package me.lenis0012.mr.commands;

import me.lenis0012.mr.MPlayer;
import me.lenis0012.mr.lang.Messages;
import me.lenis0012.mr.util.EcoUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DivorceCommand extends CommandBase {
	@Override
	public void perform(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		MPlayer mp = plugin.getMPlayer(player);
		if(!mp.isMarried()) {
			error(player, Messages.NO_PARTNER);
			return;
		}
		
		if(plugin.eco) {
			double a = EcoUtil.getPriceFromConfig("divorce");
			if(a != 0.0) {
				if(EcoUtil.withrawMoneyIfEnough(player, a)) {
					return;
				}
			}
		}
		
		String user = player.getName();
		String partner = mp.getPartner();
		Player op = Bukkit.getPlayer(partner);
		
		//Chat fix start
		if(mp.isChatting())
			mp.setChatting(false);
		if(op != null && op.isOnline()) {
			MPlayer omp = plugin.getMPlayer(op);
			if(omp.isChatting())
				omp.setChatting(false);
		}
		//chat fix end
		
		mp.divorce();
		String msg = Messages.DIVORCED.replace("{USER1}", user).replace("{USER2}", partner);
		Bukkit.getServer().broadcastMessage(ChatColor.RED + msg);
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean playersOnly() {
		return true;
	}
}
