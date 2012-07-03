package net.croxis.plugins.civileconation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import net.croxis.plugins.civilmineation.components.Ent;
import net.croxis.plugins.civilmineation.events.NewCityEvent;
import net.croxis.plugins.civilmineation.events.NewCivEvent;
import net.croxis.plugins.civilmineation.events.ResidentJoinEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Civileconation extends JavaPlugin implements Listener {
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    public void onEnable() {
    	setupDatabase();
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    public void newEcon(int entityID, String name){
    	if (getDatabase().find(EconomyComponent.class).where().eq("entityID", entityID).findUnique() == null){
	    	EconomyComponent econ = new EconomyComponent();
	    	econ.setName(name);
	    	econ.setEntityID(getDatabase().find(Ent.class).where().eq("id", entityID).findUnique());
	    	getDatabase().save(econ);
    	}
    }

    @EventHandler
    public void onResidentJoin(ResidentJoinEvent event) {
    	newEcon(event.getEntityID(), event.getName());
    }
    
    @EventHandler
    public void onNewCity(NewCityEvent event) {
    	newEcon(event.getEntityID(), event.getName());
    }
    
    @EventHandler
    public void onNewCiv(NewCivEvent event) {
    	newEcon(event.getEntityID(), event.getName());
    }
    
    @Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(EconomyComponent.class);
		return list;
	}
    
    private void setupDatabase()
	{
		try
		{
			getDatabase().find(EconomyComponent.class).findRowCount();
		}
		catch(PersistenceException ex)
		{
			System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
			installDDL();
		}
	}
}

