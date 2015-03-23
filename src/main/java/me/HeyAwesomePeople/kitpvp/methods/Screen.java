package me.HeyAwesomePeople.kitpvp.methods;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R2.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Screen {
	public void sendTitle(Player player, String title, String subtitle,
			int fadeIn, int stay, int fadeOut) {
		CraftPlayer craftplayer = (CraftPlayer) player;
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		IChatBaseComponent titleJSON = ChatSerializer.a("{'text': '"
				+ ChatColor.translateAlternateColorCodes('&', title) + "'}");
		IChatBaseComponent subtitleJSON = ChatSerializer.a("{'text': '"
				+ ChatColor.translateAlternateColorCodes('&', subtitle) + "'}");
		PacketPlayOutTitle length = new PacketPlayOutTitle(
				EnumTitleAction.TIMES, titleJSON, fadeIn, stay, fadeOut);
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(
				EnumTitleAction.TITLE, titleJSON, fadeIn, stay, fadeOut);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(
				EnumTitleAction.SUBTITLE, subtitleJSON, fadeIn, stay, fadeOut);
		connection.sendPacket(titlePacket);
		connection.sendPacket(length);
		connection.sendPacket(subtitlePacket);
	}

	public void sendActionBar(Player p, String msg) {
		IChatBaseComponent cbc = ChatSerializer
				.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}

	public void sendHeaderAndFooter(Player p, String head, String foot) {
		CraftPlayer craftplayer = (CraftPlayer) p;
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		IChatBaseComponent header = ChatSerializer.a("{'color': '', 'text': '"
				+ ChatColor.translateAlternateColorCodes('&', head) + "'}");
		IChatBaseComponent footer = ChatSerializer.a("{'color': '', 'text': '"
				+ ChatColor.translateAlternateColorCodes('&', foot) + "'}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field headerField = packet.getClass().getDeclaredField("a");
			headerField.setAccessible(true);
			headerField.set(packet, header);
			headerField.setAccessible(!headerField.isAccessible());

			Field footerField = packet.getClass().getDeclaredField("b");
			footerField.setAccessible(true);
			footerField.set(packet, footer);
			footerField.setAccessible(!footerField.isAccessible());
		} catch (Exception e) {
			e.printStackTrace();
		}
		connection.sendPacket(packet);
	}
}
