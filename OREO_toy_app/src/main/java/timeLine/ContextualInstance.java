package timeLine;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.PostLoad;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import serializer.ContextualInstanceDeserializer;
import serializer.ContextualInstanceSerializer;

@JsonIdentityInfo(
	    generator = ObjectIdGenerators.PropertyGenerator.class,
	    property = "uuid", 
	    scope = ContextualInstance.class
	)
public class ContextualInstance {
	
	@JsonSerialize(using = ContextualInstanceSerializer.class)
    @JsonDeserialize(using = ContextualInstanceDeserializer.class)
	private Object instance;
	private String sessionId;
	
    private final UUID uuid = UUID.randomUUID(); // Generato alla creazione


	public ContextualInstance(Object instance, String sessionID) {
		this.setInstance(instance);
		this.sessionId = sessionID;
	}
	
	public ContextualInstance() {}
	
	public boolean isTheSameInstance(Object instanceToCompare) {
		return this.instance == instanceToCompare;
	}


    public UUID getUuid() {
        return uuid;
    }

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public String getSessionID() {
		return sessionId;
	}
	
	public String getName() {
//		System.out.println("instance class: " + getInstanceClass());
//		return getInstanceClass().toString();
		return getInstanceClass().toString().replaceFirst("^class\\s+", "");
	}

	private Class<?> getInstanceClass() {
		if (isProxy(instance.getClass()))
			return instance.getClass().getSuperclass();
		else
			return instance.getClass();
	}

	private boolean isProxy(Class<?> cls) {
		if (cls.getSimpleName().contains("Proxy$"))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContextualInstance other = (ContextualInstance) obj;
		return Objects.equals(uuid, other.uuid);
	}
	
}
