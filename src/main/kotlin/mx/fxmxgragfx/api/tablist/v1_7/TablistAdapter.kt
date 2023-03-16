package mx.fxmxgragfx.api.tablist.v1_7

import net.minecraft.server.v1_7_R4.*
import net.minecraft.util.com.mojang.authlib.GameProfile
import net.minecraft.util.com.mojang.authlib.properties.Property
import org.apache.commons.lang.StringEscapeUtils
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.spigotmc.ProtocolInjector.PacketTabHeader
import mx.fxmxgragfx.api.tablist.shared.TabAdapter
import mx.fxmxgragfx.api.tablist.shared.skin.SkinType
import java.util.*

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class TablistAdapter : TabAdapter() {
    private val profiles : MutableMap<Player, Array<GameProfile?>> = HashMap()
    private val initialized : MutableList<Player> = ArrayList()
    private fun sendPacket(player : Player, packet : Packet) {
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
        if (getMaxElements(player) != 60) {
            sendPacket(
                player, PacketTabHeader(
                    ChatSerializer.a("{text:\"" + StringEscapeUtils.escapeJava(header) + "\"}"),
                    ChatSerializer.a("{text:\"" + StringEscapeUtils.escapeJava(footer) + "\"}")
                )
            )
        }
        return this
    }

    override fun updateSkin(skinData : Array<String>, index : Int, player : Player) {
        var data: Array<String> = skinData
        val profile = profiles[player]!![index]
        val property = profile!!.properties["textures"].iterator().next()
        val entityPlayer = this.getEntityPlayer(profile)
        data =
            if (data.isNotEmpty() && data[0].isNotEmpty() && data[1].isNotEmpty()) data else SkinType.DARK_GRAY.skinData
        if (property.signature != data[1] || property.value != data[0]) {
            profile.properties.remove("textures", property)
            profile.properties.put("textures", Property("textures", data[0], data[1]))
            sendPacket(player, PacketPlayOutPlayerInfo.addPlayer(entityPlayer))
        }
    }

    override fun getMaxElements(player : Player) : Int {
        return if ((player as CraftPlayer).handle.playerConnection.networkManager.version > 5) 80 else 60
    }

    override fun sendEntryData(player : Player, axis : Int, ping : Int, text : String) : TabAdapter {
        val profile = profiles[player]!![axis]
        val entityPlayer = this.getEntityPlayer(profile!!)
        entityPlayer.ping = ping
        setupScoreboard(player, text, profile.name)
        sendPacket(player, PacketPlayOutPlayerInfo.updatePing(entityPlayer))
        return this
    }

    override fun addFakePlayers(player : Player) : TabAdapter {
        if (!initialized.contains(player)) {
            for (i in 0 until getMaxElements(player)) {
                val profile = profiles[player]!![i]
                val entityPlayer = this.getEntityPlayer(profile!!)
                sendPacket(player, PacketPlayOutPlayerInfo.addPlayer(entityPlayer))
            }
            initialized.add(player)
        }
        return this
    }

    private fun getEntityPlayer(profile : GameProfile) : EntityPlayer {
        val server = MinecraftServer.getServer()
        val interactManager = PlayerInteractManager(server.getWorldServer(0))
        return EntityPlayer(server, server.worlds.iterator().next(), profile, interactManager)
    }

    override fun hideRealPlayers(player : Player) : TabAdapter {
        for (target in Bukkit.matchPlayer("")) {
            hidePlayer(player, target)
        }
        return this
    }

    override fun hidePlayer(player : Player, target : Player) : TabAdapter {
        sendPacket(player, PacketPlayOutPlayerInfo.removePlayer((target as CraftPlayer).handle))
        return this
    }

    override fun showRealPlayers(player : Player) : TabAdapter {
        if (!initialized.contains(player)) {
            val connection = getPlayerConnection(player)
            val networkManager = connection.networkManager
            val outgoingQueueField = networkManager.javaClass.getDeclaredField("k")
            outgoingQueueField.isAccessible = true
            (outgoingQueueField[networkManager] as Queue<*>).removeIf { `object`: Any? ->
                if (`object` != null) {
                    if (`object` is PacketPlayOutNamedEntitySpawn) {
                        handlePacketPlayOutNamedEntitySpawn(player, `object`)
                        return@removeIf true
                    } else if (`object` is PacketPlayOutRespawn) {
                        handlePacketPlayOutRespawn(player)
                        return@removeIf true
                    }
                }
                false
            }
        }
        return this
    }

    override fun showPlayer(player : Player, target : Player) : TabAdapter {
        sendPacket(player, PacketPlayOutPlayerInfo.addPlayer(this.getEntityPlayer(target)))
        return this
    }

    private fun handlePacketPlayOutNamedEntitySpawn(player : Player, packet : PacketPlayOutNamedEntitySpawn) {
        val gameProfileField = packet.javaClass.getDeclaredField("b")
        gameProfileField.isAccessible = true
        val target = Bukkit.getPlayer((gameProfileField[packet] as GameProfile).id)
        if (target != null) {
            showPlayer(player, target)
        }
    }

    private fun handlePacketPlayOutRespawn(player:  Player) {
        showPlayer(player, player)
    }

    private fun getPlayerConnection(player : Player) : PlayerConnection {
        return this.getEntityPlayer(player).playerConnection
    }

    private fun getEntityPlayer(player : Player) : EntityPlayer {
        return (player as CraftPlayer).handle
    }
}