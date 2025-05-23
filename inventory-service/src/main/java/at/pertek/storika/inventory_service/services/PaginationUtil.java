package at.pertek.storika.inventory_service.services;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Slf4j
public class PaginationUtil {

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 10;

  /**
   * Creates a Pageable object from request parameters.
   *
   * @param page         The page number (0-indexed).
   * @param size         The page size.
   * @param sortBy       The field to sort by. Can be null or empty for unsorted.
   * @param sortOrder    The sort order ("asc" or "desc"). Defaults to "asc" if sortBy is provided.
   * @param defaultSortByField Optional: A default field to sort by if sortBy is not provided but sorting is desired.
   * @param allowedSortFields Optional: A set of allowed sort fields.
   * @return A {@link Pageable} instance.
   */
  public static Pageable createPageable(Integer page, Integer size, String sortBy, String sortOrder,
      String defaultSortByField, Set<String> allowedSortFields) { // New parameter

    String effectiveSortBy = null;

    if (StringUtils.hasText(sortBy)) {
      if (allowedSortFields != null && !allowedSortFields.isEmpty() &&
          !allowedSortFields.contains(sortBy.toLowerCase())) {
        log.warn("Invalid sortBy parameter '{}'. Allowed fields are: {}", sortBy, allowedSortFields);
        throw new IllegalArgumentException("Invalid sortBy parameter: " + sortBy +
            ". Allowed sort fields are: " + allowedSortFields);
      }
      effectiveSortBy = sortBy;
    }

    int pageNumber = (page != null && page >= 0) ? page : DEFAULT_PAGE_NUMBER;
    int pageSize = (size != null && size > 0) ? size : DEFAULT_PAGE_SIZE;

    Sort sort = Sort.unsorted();
    String fieldToSort = StringUtils.hasText(effectiveSortBy) ? effectiveSortBy : defaultSortByField;

    if (StringUtils.hasText(fieldToSort)) {
      Sort.Direction direction = StringUtils.hasText(sortOrder) && "desc".equalsIgnoreCase(sortOrder) ?
          Sort.Direction.DESC : Sort.Direction.ASC;
      sort = Sort.by(direction, fieldToSort);
    }

    return PageRequest.of(pageNumber, pageSize, sort);
  }

  /**
   * Creates a Pageable object from request parameters without specific sort field validation
   * and no default sort field if sortBy is not provided.
   *
   * @param page      The page number (0-indexed).
   * @param size      The page size.
   * @param sortBy    The field to sort by. Can be null or empty for unsorted.
   * @param sortOrder The sort order ("asc" or "desc"). Defaults to "asc" if sortBy is provided.
   * @return A {@link Pageable} instance.
   */
  public static Pageable createPageable(Integer page, Integer size, String sortBy, String sortOrder) {
    return createPageable(page, size, sortBy, sortOrder, null, null);
  }

}
