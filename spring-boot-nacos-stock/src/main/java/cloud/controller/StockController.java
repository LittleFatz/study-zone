package cloud.controller;

import cloud.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/{string}", method = RequestMethod.GET)
    public String stock(@PathVariable String string) {
        return "Reducing stock:" + string;
    }

    @GetMapping(value = "/deduct/{productId}/{stockCount}")
    public String deductStock(@PathVariable("productId") Long productId,
                              @PathVariable("stockCount") Integer stockCount) {
        return stockService.deductStock(productId, stockCount);
    }
}
