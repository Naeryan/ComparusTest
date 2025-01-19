package Kornienko.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * UserRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-19T14:28:31.636580897+02:00[Europe/Kyiv]", comments = "Generator version: 7.10.0")
public class UserRequest {

  @Valid
  private Map<String, String> filters = new HashMap<>();

  public UserRequest filters(Map<String, String> filters) {
    this.filters = filters;
    return this;
  }

  public UserRequest putFiltersItem(String key, String filtersItem) {
    if (this.filters == null) {
      this.filters = new HashMap<>();
    }
    this.filters.put(key, filtersItem);
    return this;
  }

  /**
   * Get filters
   * @return filters
   */
  
  @Schema(name = "filters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters")
  public Map<String, String> getFilters() {
    return filters;
  }

  public void setFilters(Map<String, String> filters) {
    this.filters = filters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRequest userRequest = (UserRequest) o;
    return Objects.equals(this.filters, userRequest.filters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRequest {\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

