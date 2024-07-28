package danieltsuzuk.com.github.amedigital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroResponse {

    private Date timestamp;
    private Integer status;
    private List<String> errors;
    private String path;
}
