package com.trogiare.utils;

import org.bson.types.ObjectId;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.Configurable;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class IdUtil {
    public static String generate()  {
        ObjectId id = new ObjectId();
        String sid = id.toHexString();
        //logger.info("Generate an ID: " + sid);
        return sid;
    }
}
