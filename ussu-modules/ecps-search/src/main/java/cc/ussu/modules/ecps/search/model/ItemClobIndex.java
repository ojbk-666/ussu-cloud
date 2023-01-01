package cc.ussu.modules.ecps.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
// @Document(indexName = "ecps-item-clob", type = "itemClob")
@Document(indexName = "ecps-item-clob")
public class ItemClobIndex {

    @Id
    private Integer itemId;

    private String itemDesc;

    private String packingList;

}
