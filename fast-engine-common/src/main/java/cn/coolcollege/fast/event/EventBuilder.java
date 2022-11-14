package cn.coolcollege.fast.event;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author pk
 */
public class EventBuilder {

    private Class type;

    private final JSONObject jsonObject;


    public EventBuilder(JSONObject jsonObject) {
        Assert.notNull(jsonObject, "jsonObject must not be null");
        this.jsonObject = jsonObject;
    }

    public EventBuilder type(Class type) {
        this.type = type;
        return this;
    }

    public Object build() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Assert.notNull(this.type, "type must not be null");

        //调用type类的构造方法(无论是公有还是私有)
        Constructor declaredConstructor = this.type.getDeclaredConstructor(this.getClass());
        declaredConstructor.setAccessible(true);
        Object o = declaredConstructor.newInstance(this);
        return o;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
