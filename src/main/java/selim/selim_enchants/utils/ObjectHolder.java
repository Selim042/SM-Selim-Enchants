package selim.selim_enchants.utils;

public class ObjectHolder<T> {

	private T value;

	public ObjectHolder(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
