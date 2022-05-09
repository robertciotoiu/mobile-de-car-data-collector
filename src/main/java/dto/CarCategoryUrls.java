package dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class CarCategoryUrls {
    String parentLink;
    List<String> categoryLink;
    Date scrapeTimestamp;

    public CarCategoryUrls(String parentLink) {
        this.parentLink = parentLink;
    }
}
