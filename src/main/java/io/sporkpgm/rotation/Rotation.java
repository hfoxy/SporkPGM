package io.sporkpgm.rotation;

import com.google.common.base.Charsets;
import io.sporkpgm.Spork;
import io.sporkpgm.map.MapBuilder;
import io.sporkpgm.rotation.exceptions.RotationLoadException;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Rotation {

	public static Rotation provide() throws RotationLoadException, IOException {
		File rotation = Config.Rotation.ROTATION;
		if(rotation.isDirectory()) {
			throw new RotationLoadException("Unable to parse '" + rotation.getPath() + "' because it is a directory.");
		}

		if(!rotation.exists()) {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rotation), "UTF8"));
			for(MapBuilder loader : Spork.getMaps()) {
				Log.info("Printing out " + loader.getName() + " into the Rotation file");
				out.write(loader.getName());
			}

			out.close();
		}

		List<MapBuilder> loaders = new ArrayList<>();
		for(String rawLine : Files.readAllLines(rotation.toPath(), Charsets.UTF_8)) {
			if(MapBuilder.getLoader(rawLine) == null) {
				Log.warning("Failed to find a map for '" + rawLine + "' in the rotation file");
				continue;
			}

			loaders.add(MapBuilder.getLoader(rawLine));
		}

		return new Rotation(loaders);
	}

}
