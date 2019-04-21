package fudan.se.lab4;


import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.dto.Ingredient;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.dto.PaymentInfo;
import fudan.se.lab4.service.impl.OrderServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


//todo 配料列表

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTests {
    private OrderServiceImpl orderService;
    private PaymentInfo paymentInfo;
    private OrderItem item;
    private Ingredient ingredient;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        orderService = new OrderServiceImpl();
    }

    @After
    public void tearDown() {
        orderService = null;
        paymentInfo = null;
        item = null;
        ingredient = null;
    }

    // 测试订单为null
    @Test
    public void testOrderNull() {
        thrown.expect(RuntimeException.class);
        orderService.pay(null);
    }

    @Test   //todo 订单的id为空不会报错？
    public void testOrderIdNull() {
        thrown.expect(RuntimeException.class);
        orderService.pay(new Order(null, new ArrayList<>()));
    }

    @Test
    public void testOrderItemNull() {
        //test OrderItem null
        thrown.expect(RuntimeException.class);
        orderService.pay(new Order("null", null));
    }


    @Test
    public void testDrinkNameNull() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        item = new OrderItem(null, new ArrayList<>(), 1);
        orderItems.add(item);
        orderService.pay(new Order("nullDrinkName", orderItems));
    }

    @Test
    public void testDrinkNameError() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.FAILED_GET_DRINK);
        item = new OrderItem("error", new ArrayList<>(), 1);
        orderItems.add(item);
        orderService.pay(new Order("errorName", orderItems));
    }

    // size 不存在为null的情况
    @Test
    public void testOrderItemSizeSmall() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_SIZE);
        item = new OrderItem("espresso", new ArrayList<>(), 0);
        orderItems.add(item);
        orderService.pay(new Order("smallSize", orderItems));
    }

    @Test
    public void testOrderItemSizeLarge() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_SIZE);
        item = new OrderItem("espresso", new ArrayList<>(), 4);
        orderItems.add(item);
        orderService.pay(new Order("largeSize", orderItems));
    }

    @Test
    public void testOrderIngredientNull() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        item = new OrderItem("espresso", null, 1);
        orderItems.add(item);
        orderService.pay(new Order("nullIngredient", orderItems));
    }


    @Test
    public void testOrderIngredientNameNull() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        String name = null;
        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, name));
        ingredient = new Ingredient(name, 1);
        ingredients.add(ingredient);
        item = new OrderItem("espresso", ingredients, 1);
        orderItems.add(item);
        orderService.pay(new Order("nullIngredientName", orderItems));
    }

    @Test
    public void testOrderIngredientNameError() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        String name = "error";
        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, name));
        ingredient = new Ingredient(name, 1);
        ingredients.add(ingredient);
        item = new OrderItem("espresso", ingredients, 1);
        orderItems.add(item);
        orderService.pay(new Order("nullIngredientName", orderItems));
    }

    @Test
    public void testOrderItem() {
        List<OrderItem> orderItems = new ArrayList<>();

        //test 0 OrderItem
        paymentInfo = orderService.pay(new Order("0", orderItems));
        assertEquals(new PaymentInfo(0, 0, 0, new ArrayList<>()), paymentInfo);

        item = new OrderItem("espresso", new ArrayList<>(), 1);

        //test 1 OrderItem
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertEquals(new PaymentInfo(24, 0, 24, new ArrayList<>()), paymentInfo);

        //test 3 OrderItem
        orderItems.add(item);
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("3", orderItems));
        assertEquals(new PaymentInfo(70, 0, 70, new ArrayList<>()), paymentInfo);
    }

    @Test
    public void tt(){
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        item = new OrderItem("espresso",ingredients,3);
        orderItems.add(item);
        item = new OrderItem("espresso",ingredients,3);
        orderItems.add(item);
        item = new OrderItem("espresso",ingredients,3);
        orderItems.add(item);
        item = new OrderItem("espresso",ingredients,3);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("0", orderItems));
        double a = paymentInfo.getDiscount();
        double b = paymentInfo.getPrice();
        double c = paymentInfo.getDiscountPrice();
        int debug = 0;

    }


    @Test
    public void testIngredient() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        item = new OrderItem("espresso", ingredients, 1);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("0", orderItems));
        assertEquals(new PaymentInfo(24, 0, 24, new ArrayList<>()), paymentInfo);

        orderItems.clear();
        ingredient = new Ingredient("cream", 1);
        ingredients.add(ingredient);
        item = new OrderItem("espresso", ingredients, 1);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("0", orderItems));
        assertEquals(new PaymentInfo(25, 0, 25, new ArrayList<>()), paymentInfo);

        orderItems.clear();
        ingredient = new Ingredient("milk", 2);
        ingredients.add(ingredient);
        item = new OrderItem("espresso", ingredients, 1);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("0", orderItems));
        assertEquals(new PaymentInfo(27.4, 0, 27.4, new ArrayList<>()), paymentInfo);
    }

    @Test
    public void testEspressoPreferential() {
        //1.只有一大杯
        //2.一大杯加一小杯
        //3.两大杯，其中一杯加入配料
        //4.三大杯

        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();

        //1.一大杯
        item = new OrderItem("espresso", ingredients, 3);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("espresso", orderItems));
        assertEquals(new PaymentInfo(28, 0, 28, new ArrayList<>()), paymentInfo);

        //4.一小杯
        item = new OrderItem("espresso", ingredients, 1);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("espresso", orderItems));
        assertEquals(new PaymentInfo(52, 0, 52, new ArrayList<>()), paymentInfo);

        //2.增加加入配料的一大杯
        ingredient = new Ingredient("cream", 1);
        ingredients.add(ingredient);
        item = new OrderItem("espresso", ingredients, 3);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("espresso", orderItems));
        assertEquals(new PaymentInfo(57, 8.8, 48.2, new ArrayList<>()), paymentInfo);

        //3.增加配料的一大杯
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("espresso", orderItems));
        assertEquals(new PaymentInfo(86, 8.8, 77.2, new ArrayList<>()), paymentInfo);


    }

    @Test // todo 对卡布奇诺的杯型和顺序，以及配料(第二杯半价)
    public void testCappuccinoPreferential() {
        //1.一小杯
        //2.一小杯加一大杯
        //3.一小杯加一大杯，其中一杯加入配料

        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        item = new OrderItem("cappuccino", ingredients, 1);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("cappuccino", orderItems));
        assertEquals(new PaymentInfo(22, 0, 22, new ArrayList<>()), paymentInfo);

        ingredient = new Ingredient("cream", 1);
        ingredients.add(ingredient);
        item = new OrderItem("cappuccino", ingredients, 3);
        orderItems.add(item);
        paymentInfo = orderService.pay(new Order("cappuccino", orderItems));
        assertEquals(new PaymentInfo(22, 0, 22, new ArrayList<>()), paymentInfo);
    }

    @Test
    public void testTeaPreferential() {
        //1.全部红茶
        //2.全部绿茶
        //3.其中至少有一杯并且不全是红茶

        //4.数量：两杯，四杯，五杯
    }


    @Test
    public void testFullReductionPreferential(){
        //不到满减
        //到满减，且该优惠方式为最优
    }

    @Test
    public void testOptimalPreferential(){
        //1.两个优惠都有，但是满减最优
        //2.两个优惠都有，但是组合优惠最优
        //3.两个优惠都有，但是两个优惠方式一样
    }
}
