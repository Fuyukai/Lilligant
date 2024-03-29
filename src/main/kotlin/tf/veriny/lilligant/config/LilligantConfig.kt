/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.config

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Registries
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Identifier

public data class EntityBlockerConfig(
    /** The list of entity IDs to block from creation. */
    public val blockedEntities: List<Identifier>
) {
    /**
     * Checks if the provided [EntityType] should be blocked from spawning.
     */
    public fun shouldBlock(type: EntityType<*>): Boolean {

        // the loot table ID is always cached on the entitytype, it's just the entity id with
        // ``entities/`` prepended.
        val id = type.lootTableId

        for (blocked in this.blockedEntities) {
            if (id.namespace != blocked.namespace) continue
            val realPath = id.path.substring(9 /* "entities/".length */)
            if (blocked.path == realPath) return true
        }

        return false
    }
}

public data class ContentConfiguration(
    /** If true, then the Aerial Affinity enchantment will be added to the game. */
    public val addAerialAffinity: Boolean,
    /** Adds the tag enchantment interceptor. */
    public val addTagEnchantmentInterceptor: Boolean,
    /** The ticks for the sniffer's cooldown. */
    public val snifferCooldown: Long,

    /** If True, then peaceful mode will be forced. */
    public val forcePeaceful: Boolean,

    /** If True, the recipe book will be disabled. */
    public val disableRecipeBook: Boolean,

    /** See config file. */
    public val applyDefaultGameRules: Boolean,

    /** Configuration for the default portal formation interceptor. */
    public val portalFormation: PortalFormationConfig,
) {
    public data class PortalFormationConfig(
        public val enableHeightBasedInterceptor: Boolean,
        public val maxPortalWorldHeight: Int,
        public val minPortalWorldHeight: Int,
    )
}

/**
 * Central configuration used by Lilligant.
 */
public object LilligantConfig : ConfigContainer("lilligant") {
    public val entityBlockerConfig: EntityBlockerConfig by delegate(
        "entity_blocker",
        LilligantConfig::class.java.getResourceAsStream("/config/entity_blocker.toml")!!
    )

    public val contentConfig: ContentConfiguration by delegate(
        "content",
        LilligantConfig::class.java.getResourceAsStream("/config/content.toml")!!
    )

    override fun afterReload(server: MinecraftServer?) {
        if (server == null) return

        // brutally murder all entities that match
        for (world in server.worlds) {
            val allEntities = world.iterateEntities()
                .filterIsInstance<LivingEntity>()
                .groupBy { it.type }
            for ((type, entities) in allEntities) {
                val id = Registries.ENTITY_TYPE.getId(type)
                if (entityBlockerConfig.blockedEntities.any { it == id }) {
                    entities.forEach(LivingEntity::kill)
                }
            }
        }
    }
}
