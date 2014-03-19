package io.sporkpgm.rotation;

import io.sporkpgm.map.MapBuilder;
import io.sporkpgm.rotation.exceptions.RotationLoadException;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Rotation {


	public static Rotation provide() throws RotationLoadException, IOException {
		File rotation = Config.Rotation.ROTATION;
		if(rotation.isDirectory()) {
			throw new RotationLoadException("Unable to parse '" + rotation.getPath() + "' because it is a directory.");
		}

		if(!rotation.exists()) {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rotationsRepository), "UTF8"));
			for(MapBuilder loader : getMaps()) {
				Log.info("Printing out " + loader.getName() + " into the Rotation file");
				out.write(loader.getName());
			}

			out.close();
		}
	}

}
