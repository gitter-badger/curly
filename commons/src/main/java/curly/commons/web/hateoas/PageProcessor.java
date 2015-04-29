package curly.commons.web.hateoas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author João Pedro Evangelista
 */
public class PageProcessor {

    public static <T> Page<T> toPage(Collection<T> list, PageMetadata metadata) {
        Assert.notNull(list, "List content must be not null");
        return new PageImpl<T>(new ArrayList<>(list), new PageRequest((int) metadata.getNumber(), (int) metadata.getSize()), metadata.getTotalElements());
    }

    public static <T> Page<T> toPage(PagedResources<T> res) {
        Assert.notNull(res, "Resources content must be not null");
        PageMetadata metadata = res.getMetadata();
        return new PageImpl<T>(new ArrayList<>(res.getContent()), new PageRequest((int) metadata.getNumber(), (int) metadata.getSize()), metadata.getTotalElements());
    }


    public static <T> List<T> fromPage(Page<T> page) {
        Assert.notNull(page, "Page element must be not null");
        return page.getContent();
    }

}
