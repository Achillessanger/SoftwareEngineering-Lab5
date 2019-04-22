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

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTests {
    private OrderServiceImpl orderService;
    private PaymentInfo paymentInfo;
    private final List<String> EMPTYMSGS = new ArrayList<>();

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
    }

    // 测试订单为null
    @Test
    public void testOrderNull() {
        thrown.expect(RuntimeException.class);
        orderService.pay(null);
    }

    //todo lab4中订单的id为空或者重复不需要检查
//    @Test
//    public void testOrderIdNull() {
//        thrown.expect(RuntimeException.class);
//        orderService.pay(new Order(null, new ArrayList<>()));
//    }

    // 测试订单中饮品列表为null
    @Test
    public void testOrderItemNull() {
        //test OrderItem null
        thrown.expect(RuntimeException.class);
        orderService.pay(new Order("nullOrderItem", null));
    }

    // 测试订单中饮品数目不同的情况：0，1，3
    @Test
    public void testOrderItemByNum() {
        List<OrderItem> orderItems = new ArrayList<>();

        //test 0 OrderItem
        paymentInfo = orderService.pay(new Order("0", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(0, 0, 0, EMPTYMSGS)));

        //test 1 OrderItem
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        //test 3 OrderItem
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(66, 0, 66, EMPTYMSGS)));
    }

    // 测试饮品name为null
    @Test
    public void testDrinkNameNull() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        orderItems.add(new OrderItem(null, new ArrayList<>(), 1));
        orderService.pay(new Order("nullDrinkName", orderItems));
    }

    // 测试饮品name错误
    @Test
    public void testDrinkNameError() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.FAILED_GET_DRINK);
        orderItems.add(new OrderItem("error", new ArrayList<>(), 1));
        orderService.pay(new Order("errorName", orderItems));
    }

    // 测试四种饮品的价格
    @Test
    public void testDrinkKinds() {
        List<OrderItem> orderItems = new ArrayList<>();

        // cappuccino
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("cappuccino", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(24, 0, 24, EMPTYMSGS)));

        // espresso
        orderItems.clear();
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("espresso", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        // redTea
        orderItems.clear();
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("redTea", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(20, 0, 20, EMPTYMSGS)));

        // greenTea
        orderItems.clear();
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("greenTea", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(18, 0, 18, EMPTYMSGS)));
    }

    // 测试订单中饮品的不同的组合：1.卡布奇诺+浓缩咖啡，2.红茶+绿茶，3.咖啡+茶
    @Test
    public void testOrderItemCombination() {
        List<OrderItem> orderItems = new ArrayList<>();

        //1. // todo 多余
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("coffee", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(46, 0, 46, EMPTYMSGS)));

        //2. // todo 多余
        orderItems.clear();
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("tea", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(38, 0, 38, EMPTYMSGS)));

        //3.
        orderItems.remove(1);
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("tea", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(42, 0, 42, EMPTYMSGS)));

    }

    // 测试饮品数量size 不存在为null的情况，以下只分过大和过小的情况
    @Test
    public void testOrderItemSizeSmall() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_SIZE);
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), Integer.MIN_VALUE));
        orderService.pay(new Order("smallSize", orderItems));
    }

    @Test
    public void testOrderItemSizeLarge() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_SIZE);
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), Integer.MAX_VALUE));
        orderService.pay(new Order("largeSize", orderItems));
    }

    // 对咖啡三种杯型的测试
    @Test
    public void testCoffeeSizeCorrect() {
        List<OrderItem> orderItems = new ArrayList<>();

        // size = 1
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("size=1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        // size = 2
        orderItems.clear();
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 2));
        paymentInfo = orderService.pay(new Order("size=2", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(24, 0, 24, EMPTYMSGS)));

        // size = 3
        orderItems.clear();
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(new Order("size=3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(26, 0, 26, EMPTYMSGS)));
    }

    // 对茶三种杯型的测试
    @Test
    public void testTeaSizeRight() {
        List<OrderItem> orderItems = new ArrayList<>();

        // size = 1
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("size=1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(20, 0, 20, EMPTYMSGS)));

        // size = 2
        orderItems.clear();
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 2));
        paymentInfo = orderService.pay(new Order("size=2", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        // size = 3
        orderItems.clear();
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(new Order("size=3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(23, 0, 23, EMPTYMSGS)));
    }

    // 测试配料为null
    @Test
    public void testOrderIngredientNull() {
        List<OrderItem> orderItems = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        orderItems.add(new OrderItem("espresso", null, 1));
        orderService.pay(new Order("nullIngredient", orderItems));
    }

    // 测试配料name为null
    @Test
    public void testOrderIngredientNameNull() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        String name = null;
        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, name));
        ingredients.add(new Ingredient(name, 1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        orderService.pay(new Order("nullIngredientName", orderItems));
    }

    // 测试配料name错误
    @Test
    public void testOrderIngredientNameError() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        thrown.expect(RuntimeException.class);
        String name = "error";
        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, name));
        ingredients.add(new Ingredient(name, 1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        orderService.pay(new Order("nullIngredientName", orderItems));
    }

    // 测试配料name正确，只测一种，原因是在一个csv文件中
    @Test
    public void testOrderIngredientNameCorrect() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        paymentInfo = orderService.pay(new Order("nullIngredientName", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(23, 0, 23, EMPTYMSGS)));
    }

    // 测试配料数目为负数:-1
    @Test
    public void testIngredientSizeSmall() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();

        thrown.expect(RuntimeException.class);
        //todo 有log信息吗？
        ingredients.add(new Ingredient("cream", -1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        orderService.pay(new Order("1", orderItems));
    }

    // 测试配料的不同size
    @Test
    public void testIngredientByNum() {
        List<OrderItem> orderItems = new ArrayList<>();

        //test 0 ingredient
        List<Ingredient> ingredients1 = new ArrayList<>();
        orderItems.add(new OrderItem("espresso", ingredients1, 1));
        paymentInfo = orderService.pay(new Order("0", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        //test 1 ingredient
        orderItems.clear();
        List<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("espresso", ingredients2, 1));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(23, 0, 23, EMPTYMSGS)));

        //test 3 ingredient
        orderItems.clear();
        List<Ingredient> ingredients3 = new ArrayList<>();
        ingredients3.add(new Ingredient("cream", 3));
        orderItems.add(new OrderItem("espresso", ingredients3, 1));
        paymentInfo = orderService.pay(new Order("3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(25, 0, 25, EMPTYMSGS)));
    }

    // 测试不同配料类型的组合：milk+cream
    @Test
    public void testIngredientCombination() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("cream", 1));
        ingredients.add(new Ingredient("milk", 1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        paymentInfo = orderService.pay(new Order("0", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(24.2, 0, 24.2, EMPTYMSGS)));
    }

    // 对浓缩咖啡的优惠方式一的测试
    @Test
    public void testEspressoPromotion() {
        //1.只有一大杯
        //2.一大杯加一小杯
        //3.两大杯，其中一杯加入配料
        //4.三大杯

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(26, 0, 26, msgs)));

        //2.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("2", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(48, 0, 48, msgs)));

        //3.
        msgs.add("espresso: 每2杯,2杯8.0折");
        orderItems.remove(1);
        List<Ingredient> ingredients3 = new ArrayList<>();
        ingredients3.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("espresso", ingredients3, 3));
        paymentInfo = orderService.pay(new Order("3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(53, 8, 45, msgs)));

        //4.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(new Order("4", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(79, 8, 71, msgs)));
    }

    // 对卡布奇诺的优惠方式一的测试
    @Test
    public void testCappuccinoPromotion() {
        //1.一小杯
        //2.一小杯加一大杯
        //3.两个小杯，其中一个加入配料
        //4.三杯

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(24, 0, 24, msgs)));

        msgs.add("cappuccino: 每2杯,1杯5.0折");
        //2.
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(new Order("2", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(52, 11, 41, msgs)));

        //3.
        orderItems.remove(1);
        List<Ingredient> ingredients3 = new ArrayList<>();
        ingredients3.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("cappuccino", ingredients3, 1));
        paymentInfo = orderService.pay(new Order("3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(49, 11, 38, msgs)));

        //4.
        orderItems.remove(1);
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 2));
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(new Order("4", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(78, 11, 67, msgs)));
    }

    //todo delete
    private void printDebug(PaymentInfo paymentInfo) {
        System.out.println(paymentInfo.getPrice());
        System.out.println(paymentInfo.getDiscount());
        System.out.println(paymentInfo.getDiscountPrice());
        System.out.println(paymentInfo.getMsgs());
    }

    // 对红茶的优惠方式一的测试
    @Test
    public void testRedTeaPromotion() {
        //1.3杯小杯红茶
        //2.4杯小杯红茶
        //3.9杯小杯红茶
        //4.4杯红茶，其中一杯为大杯
        //5.4杯红茶，其中一杯加入配料

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        for (int i = 0; i < 3; i++) {
            orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(60, 0, 60, msgs)));

        msgs.add("redTea :买3杯送1杯");
        //4.
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(new Order("4", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(83, 18, 65, msgs)));

        //5.
        orderItems.remove(3);
        List<Ingredient> ingredients5 = new ArrayList<>();
        ingredients5.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("redTea", ingredients5, 3));
        paymentInfo = orderService.pay(new Order("5", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(84, 18, 66, msgs)));

        //2.
        orderItems.remove(3);
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("2", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(80, 18, 62, msgs)));

        //3.
        for (int i = 0; i < 5; i++) {
            orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(180, 36, 144, msgs)));

    }

    // todo 多余？
    @Test
    public void testGreenTeaPromotion() {
        //1.4杯小杯绿茶
        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();
        msgs.add("greenTea :买3杯送1杯");
        for (int i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(72, 16, 56, msgs)));
    }

    // 对绿茶的优惠方式一的测试
    @Test
    public void testTeaCombinationPromotion() {
        //1.满减，八杯茶，一杯红茶
        //2.满减，八杯茶，两杯红茶
        //3.满减，九杯茶，一杯红茶
        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();
        msgs.add("redTea greenTea :买3杯送1杯");

        //1.
        for (int i = 0; i < 7; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(146, 34, 112, msgs)));

        //3.
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(164, 34, 130, msgs)));

        //2.
        orderItems.remove(0);
        orderItems.remove(0);
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(148, 36, 112, msgs)));
    }

    // 对茶的优惠方式累加的测试
    @Test
    public void testFullReductionPromotion() {
        //1.<100
        //2.=100
        //3.>100

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, msgs)));

        msgs.add("满100减30.0");
        //2.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 2));
        for (int i = 0; i < 3; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("2", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(100, 30, 70, msgs)));

        //3.
        orderItems.clear();
        for (int i = 0; i < 5; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(110, 30, 80, msgs)));
    }

    @Test
    public void testCombinationPromotion() {
        //1.两种咖啡
        //2.卡布奇诺+茶
        //3.浓缩咖啡+茶
        //4.三种

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        int i = 0;

        //1.
        msgs.add("espresso: 每2杯,2杯8.0折");
        msgs.add("cappuccino: 每2杯,1杯5.0折");
        for (; i < 2; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        for (i = 0; i < 6; i++) {
            orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(196, 41, 155, msgs)));

        //2.
        msgs.clear();
        msgs.add("greenTea :买3杯送1杯");
        msgs.add("cappuccino: 每2杯,1杯5.0折");
        orderItems.clear();
        for (i = 0; i < 8; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        for (i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("2", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(192, 43, 149, msgs)));

        //3.
        msgs.clear();
        msgs.add("espresso: 每2杯,2杯8.0折");
        msgs.add("greenTea :买3杯送1杯");
        orderItems.clear();
        for (i = 0; i < 8; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        for (i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        paymentInfo = orderService.pay(new Order("3", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(196, 40, 156, msgs)));

        //4.
        orderItems.clear();
        msgs.clear();
        msgs.add("espresso: 每2杯,2杯8.0折");
        msgs.add("greenTea :买3杯送1杯");
        msgs.add("cappuccino: 每2杯,1杯5.0折");
        for (i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        for (i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        for (i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("4", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(172, 35, 137, msgs)));
    }

    // 两种优惠都有，最优选择的测试
    @Test
    public void testOptimalPromotion() {
        //1.两个优惠都有，但是满减最优：5杯红茶
        //2.两个优惠都有，但是组合优惠最优：
        //3.两个优惠都有，但是两个优惠方式一样

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        msgs.add("满100减30.0");
        for (int i = 0; i < 5; i++) {
            orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(100, 30, 70, msgs)));

        //2.
        orderItems.clear();
        msgs.clear();
        msgs.add("redTea greenTea :买3杯送1杯");
        for (int i = 0; i < 7; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(new Order("1", orderItems));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(146, 34, 112, msgs)));

        //3.
        orderItems.clear();
        msgs.clear();
        msgs.add("greenTea :买3杯送1杯");
        for (int i = 0; i < 20; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(new Order("3", orderItems));
        printDebug(paymentInfo);
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(360, 90, 270, msgs)));

    }


    private boolean paymentInfoEquals(PaymentInfo pay1, PaymentInfo pay2) {
        if (pay1 == pay2) {
            return true;
        }
        if (null == pay1 || null == pay2) {
            return false;
        }
        if (Math.abs(pay1.getPrice() - pay2.getPrice()) > 0.01) {
            return false;
        }
        if (Math.abs(pay1.getDiscount() - pay2.getDiscount()) > 0.01) {
            return false;
        }
        if (Math.abs(pay1.getDiscountPrice() - pay2.getDiscountPrice()) > 0.01) {
            return false;
        }
        List<String> msgs = pay1.getMsgs();
        if (msgs == null || !msgs.equals(pay2.getMsgs())) {
            return false;
        }
        return true;
    }
}
