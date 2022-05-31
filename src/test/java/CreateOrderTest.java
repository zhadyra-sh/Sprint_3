import client.BaseHttpClient;
import client.OrderApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Order;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final OrderApi api = new OrderApi();

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseHttpClient.BASE_URL;
    }

    private final Order order;
    private final Object expectedCode;

    public CreateOrderTest(Order order, Object expectedResponseStatusCode) {
        this.order = order;
        this.expectedCode = expectedResponseStatusCode;
    }

    @Parameterized.Parameters(name = "Тестовые данные {0} {1} {2}")
    public static Object[][] getColorsData() {
        return new Object[][]{
                {getOrderWithoutColor(), HttpStatus.SC_CREATED}, // можно совсем не указывать цвет
                {getOrderWithBlackColor(), HttpStatus.SC_CREATED}, // можно указать один из цветов — BLACK или GREY
                {getOrderWithTwoColors(), HttpStatus.SC_CREATED} // можно указать оба цвета
        };
    }

    @Test
    @DisplayName("Должна быть возможность сделать заказ с разным набором выбранных цветов")
    public void createOrderTest() {
        api
                .createOrder(this.order)
                .then().assertThat().statusCode((int) this.expectedCode)
                .and().extract().body().path("track");
    }

    public static Order getOrderWithoutColor() {
        return new Order(
                "Alexandra",
                "Chirkova",
                "Lenina, 96 apt.",
                "3",
                "+7 705 555 55 55",
                4,
                "2022-07-07",
                "Privet Mir"
        );
    }

    public static Order getOrderWithBlackColor() {
        Order orderBlack = getOrderWithoutColor();
        orderBlack.setColor(new String[]{"BLACK"});
        return orderBlack;
    }

    public static Order getOrderWithTwoColors() {
        Order orderTwoColor = getOrderWithoutColor();
        orderTwoColor.setColor(new String[]{"BLACK", "GREY"});
        return orderTwoColor;
    }
}
