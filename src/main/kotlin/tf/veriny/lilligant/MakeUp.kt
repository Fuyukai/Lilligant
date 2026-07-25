package tf.veriny.lilligant

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import tf.veriny.lilligant.enchant.EnchantmentEffectInterceptor
import tf.veriny.lilligant.enchant.TaggedStatusEnchantmentInterceptor
import tf.veriny.lilligant.pack.KamuidromeMetadata
import java.lang.management.ManagementFactory

// this is where i would use multiple entrypoints... if i had one.......

@Mod("lilligant")
public object MakeUp {
    @JvmStatic
    private val LOGGER = LogManager.getLogger(MakeUp::class.java)

    @JvmStatic
    private val FLAG_ARGS = mutableSetOf(
        "-XX:+UseG1GC",
        "-Daikars.new.flags=true",
        "-Dusing.aikars.flags=https://mcflags.emc.gs",
        "-XX:MaxTenuringThreshold=1",
    )
    public var PEBKAC_ARGS: Set<String> = setOf()
        private set

    /** Tag for flower blocks that should have plant logic overriden. */
    public val OVERRIDE_PLANT_CHECK: TagKey<Block> =
        TagKey.of(RegistryKeys.BLOCK, id("override_plant_check"))

    /** Tag for valid blocks that plants in the previous tag can be placed on. */
    public val FLOWER_PLANT_BLOCKS: TagKey<Block> =
        TagKey.of(RegistryKeys.BLOCK, id("flower_plant_blocks"))


    public fun id(name: String): Identifier {
        return Identifier("lilligant", name)
    }

    private fun checkJvmArgs() {
        val args = ManagementFactory.getRuntimeMXBean().inputArguments
        val intersection = FLAG_ARGS.intersect(args.toSet())

        if (intersection.isNotEmpty()) {
            LOGGER.error("PEBKAC detected! Your warranty is now voided!")
            LOGGER.error("Detected problematic flags: ${intersection.joinToString(", ")}")
            PEBKAC_ARGS = intersection
        } else {
            LOGGER.info("Thank you for not messing with your JVM args :)")
        }
    }

    init {
        EnchantmentEffectInterceptor.addInterceptor(TaggedStatusEnchantmentInterceptor)
        checkJvmArgs()

        val metadata = KamuidromeMetadata.METADATA
        if (metadata != null) {
            LOGGER.info("Detected Kamuidrome metadata!")
            LOGGER.info("KAMUIDROME #1: [${metadata.packName}] [${metadata.packVersion}]")
            if (metadata.gitInfo == null) {
                LOGGER.info("KAMUIDROME #2: [N/A] [N/A]")
            } else {
                LOGGER.info("KAMUIDROME #2: [${metadata.gitInfo.commitHash}] [${metadata.gitInfo.branch}]")
            }
        }
    }
}