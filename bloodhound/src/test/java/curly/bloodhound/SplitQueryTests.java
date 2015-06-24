package curly.bloodhound;

import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author João Evangelista
 */
public class SplitQueryTests {


	public static final String REGEX_INNER_CURLY = "\\{(.*?)\\}";

	public static final String SPLIT_SPACES = "\\s+(?![^\\{]*\\})";

	@Test
	public void testSplitSpace() throws Exception {
		List<String> list = getSplits();
		System.out.println(list);
		assertThat(list.size(), is(3));
		assertThat(list, contains("foo:bar", "goo:{too, yoo,loo}", "roo:{ree rii}"));
	}

	private List<String> getSplits() {
		String s = "foo:bar goo:{too, yoo,loo} roo:{ree rii}";
		return Arrays.asList(s.split(SPLIT_SPACES));
	}

	@Test
	public void testKeyValue() throws Exception {
		Map<String, String> kvMap = getMappedKeyValue();
		assertTrue(kvMap.get("foo").equals("bar"));
	}

	private Map<String, String> getMappedKeyValue() {
		List<String> list = getSplits();
		Map<String, String> kvMap = new HashMap<>(0);
		list.forEach(item -> {
			String[] kv = item.split(":");
			if (kv.length == 2) {
				kvMap.put(kv[0], kv[1]);
			}
		});
		System.out.println("Mapped Key Value " + kvMap);
		return kvMap;
	}

	@Test
	public void testIfContainsCurlyCleanAndSplitIntoMap() throws Exception {
		Map<String, String> kvMap = getMappedKeyValue();
		final MultiValueMap<String, String> multimap = new LinkedMultiValueMap<>();
		if (kvMap != null) {
			kvMap.forEach((key, value) -> {
				if (value.contains("{") && value.contains("}")) {
					Matcher matcher = Pattern.compile(REGEX_INNER_CURLY).matcher(value);
					if (matcher.find()) {
						String inner = matcher.group();
						String cleaned = inner.replace("{", "").replace("}", "");
						if (cleaned.contains(",")) {
							multimap.put(key, trimElements((Arrays.asList(cleaned.split(",")))));
						} else {
							multimap.put(key, Arrays.asList(cleaned.split(" ")));
						}
					}
				} else {
					multimap.add(key, value);
				}
			});
		}
		System.out.println("Output MultiMap" + multimap);
		assertThat(multimap.get("foo"), contains("bar"));
	}

	private List<String> trimElements(List<String> strings) {
		if (strings == null) return null;
		List<String> output = new ArrayList<>(strings.size());
		strings.stream().forEach(e -> output.add(e.trim()));
		return output;
	}

}
