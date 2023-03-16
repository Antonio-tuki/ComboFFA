package mx.fxmxgragfx.api.tablist.v1_8

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.server.v1_8_R3.*
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import mx.fxmxgragfx.api.tablist.shared.TabAdapter
import mx.fxmxgragfx.api.tablist.shared.client.ClientVersionImpl.getProtocolVersion
import mx.fxmxgragfx.api.tablist.shared.skin.SkinType
import java.util.*

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class TablistAdapter : TabAdapter() {
    private val profiles : MutableMap<Player, Array<GameProfile?>> = HashMap()
    private val initialized : MutableList<Player> = ArrayList()
    private fun sendPacket(player : Player, packet : Packet<*>) {
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun createProfiles(index : Int, text : String, player : Player) {
        if (!profiles.containsKey(player)) {
            profiles[player] = arrayOfNulls(80)
        }
        if (profiles[player]!!.size < index + 1 || profiles[player]!![index] == null) {
            val profile = GameProfile(UUID.randomUUID(), text)
            val skinData = SkinType.DARK_GRAY.skinData
            profile.properties.put("textures", Property("textures", skinData[0], skinData[1]))
            profiles[player]!![index] = profile
        }
    }

    override fun sendHeaderFooter(player : Player, header : String, footer : String) : TabAdapter {
        if (getMaxElements(player) > 60) {
            val packet: Packet<*> = PacketPlayOutPlayerListHeaderFooter(
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"$header\"}")
            )
            try {
                val footerField = packet.javaClass.getDeclaredField("b")
                footerField.isAccessible = true
                footerField[packet] = IChatBaseComponent.ChatSerializer.a("{\"text\":\"$footer\"}")
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
            sendPacket(player, packet)
        }
        return this
    }

    override fun updateSkin(skinData : Array<String>, index : Int, player : Player) {
        val profile = profiles[player]!![index]
        val property = profile!!.properties["textures"].iterator().next()
        val entityPlayer = this.getEntityPlayer(profile)
        var data: Array<String> = if (skinData.isNotEmpty() && skinData[0].isNotEmpty() && skinData[1].isNotEmpty()) skinData else SkinType.DARK_GRAY.skinData
        if (property.signature != data[1] || property.value != data[0]) {
            profile.properties.remove("textures", property)
            profile.properties.put("textures", Property("textures", data[0], data[1]))
            this.sendInfoPacket(player, EnumPlayerInfoAction.ADD_PLAYER, entityPlayer)
        }
    }

    override fun getMaxElements(player : Player) : Int {
        val version = getProtocolVersion(player)
        return if (version == -1 || version > 5) 80 else 60
    }

    override fun sendEntryData(player : Player, axis : Int, ping : Int, text : String) : TabAdapter {
        val profile = profiles[player]!![axis]
        val entityPlayer = this.getEntityPlayer(profile!!)
        entityPlayer.ping = ping
        setupScoreboard(player, text, profile.name)
        if (getMaxElements(player) == 80) {
            entityPlayer.listName = ChatComponentText(text)
            this.sendInfoPacket(player, EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, entityPlayer)
        }
        this.sendInfoPacket(player, EnumPlayerInfoAction.UPDATE_LATENCY, entityPlayer)
        return this
    }

    override fun addFakePlayers(player : Player) : TabAdapter {
        if (!initialized.contains(player)) {
            for (i in 0 until getMaxElements(player)) {
                val profile = profiles[player]!![i]
                val entityPlayer = this.getEntityPlayer(profile!!)
                this.sendInfoPacket(player, EnumPlayerInfoAction.ADD_PLAYER, entityPlayer)
            }
            initialized.add(player)
        }
        return this
    }

    private fun getEntityPlayer(profile : GameProfile) : EntityPlayer {
        val server = MinecraftServer.getServer()
        val interactManager = PlayerInteractManager(server.getWorldServer(0))
        return EntityPlayer(server, server.getWorldServer(0), profile, interactManager)
    }

    override fun hideRealPlayers(player : Player) : TabAdapter {
        Bukkit.getServer().onlinePlayers.forEach { target: Player -> hidePlayer(player, target) }
        return this
    }

    override fun hidePlayer(player : Player, target : Player) : TabAdapter {
        this.sendInfoPacket(player, EnumPlayerInfoAction.REMOVE_PLAYER, target)
        return this
    }

    override fun showRealPlayers(player : Player) : TabAdapter {
        if (!initialized.contains(player)) {
            val pipeline = getPlayerConnection(player).networkManager.channel.pipeline()
            pipeline.addBefore(
                "packet_handler",
                player.name,
                createShowListener(player)
            )
        }
        return this
    }

    private fun createShowListener(player : Player) : ChannelDuplexHandler {
        return object : ChannelDuplexHandler() {
            @Throws(Exception::class)
            override fun write(context: ChannelHandlerContext, packet: Any, promise: ChannelPromise) {
                if (packet is PacketPlayOutNamedEntitySpawn) {
                    val uuidField = packet.javaClass.getDeclaredField("b")
                    uuidField.isAccessible = true
                    val target = Bukkit.getPlayer(uuidField[packet] as UUID)
                    target?.let { showPlayer(player, it) }
                } else if (packet is PacketPlayOutRespawn) {
                    showPlayer(player, player)
                }
                super.write(context, packet, promise)
            }
        }
    }

    override fun showPlayer(player : Player, target : Player) : TabAdapter {
        this.sendInfoPacket(player, EnumPlayerInfoAction.ADD_PLAYER, target)
        return this
    }

    private fun getPlayerConnection(player : Player) : PlayerConnection {
        return this.getEntityPlayer(player).playerConnection
    }

    private fun sendInfoPacket(player : Player, action : EnumPlayerInfoAction, target : EntityPlayer) {
        sendPacket(player, PacketPlayOutPlayerInfo(action, target))
    }

    private fun sendInfoPacket(player : Player, action : EnumPlayerInfoAction, target : Player) {
        this.sendInfoPacket(player, action, (target as CraftPlayer).handle)
    }

    private fun getEntityPlayer(player: Player): EntityPlayer {
        return (player as CraftPlayer).handle
    }
}