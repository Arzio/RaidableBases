package com.arzio.raidablebases.data.loader;

import java.io.File;
import java.io.IOException;

import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.RaidableBases;
import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BaseDamageProvider;
import com.arzio.raidablebases.data.impl.BaseDamageImpl;

/**
 * Loader for bases. You are able to implement it as you want.<br>
 * This loader is used by {@link BaseDamageProvider} to load and save bases in
 * disk.
 * 
 * @author Arzio
 */
public interface BaseIOLoader {

	/**
	 * Saves a {@link BaseDamage} to a {@link File} in disk. If you are using any
	 * database system, you should take note this operation runs in the main thread
	 * in a for-loop for every dirty bases.<br>
	 * <br>
	 * When the server is shutting down, the plugin tries to save all the remaining
	 * dirty bases in disk, which means you will need to take care about async tasks
	 * during the server shutdown process. If needed, you can get the
	 * {@link RaidableBases} instance and check if the plugin is enabled before
	 * saving any base in a async way.
	 * 
	 * @param baseFile The base file in disk
	 * @param baseDamage The base damage data
	 * @throws IOException If any IO exception occurs
	 */
	public void save(File baseFile, BaseDamage baseDamage) throws IOException;

	/**
	 * Loads a {@link BaseDamage} by using a {@link Base} as a reference.
	 * This method should instance a new {@link BaseDamage} implementation
	 * like {@link BaseDamageImpl}, read it fully with any way you want
	 * and return it.
	 * 
	 * @param baseFile The base file in disk
	 * @param base The base used as a reference
	 * @return 
	 * @throws IOException
	 */
	public BaseDamage load(File baseFile, Base base) throws IOException;

	/**
	 * Gets the base file in according to this loader rules.
	 * 
	 * @param folder
	 * @param base
	 * @return
	 */
	public File getBaseFile(File folder, Base base);
}
