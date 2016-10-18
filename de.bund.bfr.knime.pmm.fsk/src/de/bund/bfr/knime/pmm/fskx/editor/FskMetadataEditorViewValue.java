package de.bund.bfr.knime.pmm.fskx.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import de.bund.bfr.knime.pmm.fskx.FskMetaData;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FskMetadataEditorViewValue extends JSONViewContent {
	
	FskMetaData metadata;
	
	@Override
	public void saveToNodeSettings(NodeSettingsWO settings){
		
		try (ByteArrayOutputStream b = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(b)) {
			os.writeObject(metadata);
			byte[] array = b.toByteArray();
			settings.addByteArray("metadata", array);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		
		byte[] array = settings.getByteArray("metadata");
		
		try (ByteArrayInputStream b = new ByteArrayInputStream(array);
				ObjectInputStream o = new ObjectInputStream(b)) {
			metadata = (FskMetaData) o.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FskMetadataEditorViewValue other = (FskMetadataEditorViewValue) obj;
		
		return Objects.equals(metadata, other.metadata);
	}
	
	@Override
	public int hashCode() {
		return metadata.hashCode();
	}
}
