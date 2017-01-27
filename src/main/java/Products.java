import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="products")
public class Products {
    private List<Product> products;

    @XmlElement(name="product")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> items) {
        this.products = products;
    }
}
