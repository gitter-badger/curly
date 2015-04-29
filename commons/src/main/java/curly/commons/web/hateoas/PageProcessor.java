package curly.commons.web.hateoas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author João Pedro Evangelista
 */
public class PageProcessor {

    public static <T> Page<T> toPage(List<T> list, Pageable pageable) {
        Assert.notNull(list, "List content must be not null");
        return new PageImpl<T>(list, pageable, list.size());
    }

    public static <T> List<T> fromPage(Page<T> page) {
        Assert.notNull(page, "Page element must be not null");
        return page.getContent();
    }
}
