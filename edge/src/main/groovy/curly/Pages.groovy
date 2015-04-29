package curly

import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.Chunk
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.Assert

/**
 * @author João Pedro Evangelista
 */
class Pages<T> extends Chunk<T> implements Page<T> {


    private final long total;
    private final Pageable pageable;

    /**
     * Constructor of {@code Pages}.
     *
     * @param content the content of this page, must not be {@literal null}.
     * @param pageable the paging information, can be {@literal null}.
     * @param total the total amount of items available
     */
    public Pages(List<T> content, Pageable pageable, long total) {

        super(content, pageable);

        Assert.isTrue(total >= content.size(), "Total must not be less than the number of elements given!");

        this.total = total;
        this.pageable = pageable;
    }

    /**
     * Creates a new {@link Pages} with the given content. This will result in the created {@link Page} being identical
     * to the entire {@link List}.
     *
     * @param content must not be {@literal null}.
     */
    public Pages(List<T> content) {
        this(content, null, null == content ? 0 : content.size());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#getTotalPages()
     */

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#getTotalElements()
     */

    @Override
    public long getTotalElements() {
        return total;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#hasNext()
     */

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#isLast()
     */

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#transform(org.springframework.core.convert.converter.Converter)
     */

    @Override
    public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
        return new Pages<S>(getConvertedContent(converter), pageable, total);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {

        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getNumber(), getTotalPages(), contentType);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Pages<?>)) {
            return false;
        }

        Pages<?> that = (Pages<?>) obj;

        return this.total == that.total && super.equals(obj);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {

        int result = 17;

        result += 31 * (int) (total ^ total >>> 32);
        result += 31 * super.hashCode();

        return result;
    }


}
