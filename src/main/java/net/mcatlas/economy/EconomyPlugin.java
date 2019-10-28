package net.mcatlas.economy;

import net.mcatlas.economy.storage.AccountStorage;
import net.mcatlas.economy.storage.SQLiteAccountStorage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyPlugin extends JavaPlugin {

    private Economy economy;
    private AccountStorage accountStorage;

    @Override
    public void onEnable() {
        Plugin vaultPlugin = getServer().getPluginManager().getPlugin("Vault");

        if (vaultPlugin == null) {
            disablePlugin("Vault must be installed");
            return;
        }

        if (!setupEconomy()) {
            disablePlugin("Failed to setup Vault economy");
            return;
        }

        if (!setupAccountStorage()) {
            disablePlugin("Failed to setup Account Storage");
            return;
        }



    }

    @Override
    public void onDisable() {

    }

    public Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            getLogger().warning("RegisteredServiceProvider was null");
            return false;
        }

        economy = rsp.getProvider();

        if (economy == null) {
            return false;
        }

        Economy economyImp = new GoldIngotEconomy();

        getServer().getServicesManager().register(Economy.class, economyImp, this, ServicePriority.Highest);
        getServer().getLogger().info("Using " + economyImp.getClass().getName() + " as the Economy implementation");

        return true;
    }

    private boolean setupAccountStorage() {
        accountStorage = new SQLiteAccountStorage();

        return true;
    }

    private void disablePlugin(String reason) {
        getLogger().warning("Disabling for: " + reason);
        getServer().getPluginManager().disablePlugin(this);
    }

}
