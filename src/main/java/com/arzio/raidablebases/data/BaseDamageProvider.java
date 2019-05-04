package com.arzio.raidablebases.data;

import org.bukkit.event.Listener;

import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.data.loader.BaseIOLoader;

/**
 * A provider of {@link BaseDamage}.<br>
 * Acting as a {@link BaseDamage} manager, you can get access to every
 * {@link BaseDamage} from it and also execute some general actions that only a
 * manager object could do.
 * 
 * @author Arzio
 *
 */
public interface BaseDamageProvider extends Listener {

	/**
	 * Saves all the dirty bases.<br>
	 * In other words, it saves all the bases that are waiting to be saved
	 * and sets the <code>isDirty()</code> flag of these bases to false.
	 */
	public void saveDirty();

	/**
	 * Gets the base information about a base.
	 * @param base The base you want to get the damage information.
	 * @return A {@link BaseDamage} instance. Should never return <code>null</code>.
	 */
	public BaseDamage getBaseDamage(Base base);

	/**
	 * Gets the current {@link BaseIOLoader} instance.<br>
	 * @return
	 */
	public BaseIOLoader getLoader();

	/**
	 * Sets the current {@link BaseIOLoader} to be used during the bases I/O.
	 * @param handler The loader instance.
	 */
	public void setLoader(BaseIOLoader handler);

}
