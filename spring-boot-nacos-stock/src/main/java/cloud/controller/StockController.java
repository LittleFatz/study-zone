package cloud.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @RequestMapping(value = "/stock/{string}", method = RequestMethod.GET)
    public String stock(@PathVariable String string) {
        return "Reducing stock:" + string;
    }
}
