import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;

public class Stuff {
	public String string = "protocol://user@example.net:243/food?type=grain&name=corn#silk";
	public String[] strings = {"text 1", "text 2", "multiline\nstring"};
	public byte byt = 39;
	public long integer = Integer.MAX_VALUE + 1L;
	public double floa = 123.45;
	public char character = 'j';
	public boolean thisSentence = false;
	public int[] fibonacci = {1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144};
	public Map<String, Object> numbers = Map.of(
		"prime", List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43),
		"square", new int[]{0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144}
	);
	public Object anonymousObject = new Object() {
		private final String secretStuff = "e1b5b8939d7d77a24e200fcdce367d2a73f801d8678208e3096d4d5136ff4c19";
		private final String[] authorizedPersonnel = {"C J", "A B"};
	};
	public RetentionPolicy policy = RetentionPolicy.RUNTIME;
}
