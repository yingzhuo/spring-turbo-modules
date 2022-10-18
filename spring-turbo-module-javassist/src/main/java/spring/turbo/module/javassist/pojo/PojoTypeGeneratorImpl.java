/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.javassist.pojo;

import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.module.javassist.AnnotationDescriptor;
import spring.turbo.module.javassist.AnnotationDescriptorHelper;
import spring.turbo.util.Asserts;
import spring.turbo.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.2.2
 */
public class PojoTypeGeneratorImpl implements PojoTypeGenerator {

    private final Map<String, Class<?>> classCache = new HashMap<>();

    /**
     * 构造方法
     */
    public PojoTypeGeneratorImpl() {
        super();
    }

    @Override
    public synchronized Class<?> generate(PojoDescriptor classDescription) {

        Asserts.notNull(classDescription);

        try {
            return doGenerate(classDescription);
        } catch (NotFoundException e) {
            // TODO: 处理异常
            throw new RuntimeException(e);
        } catch (CannotCompileException e) {
            // TODO: 处理异常
            throw new RuntimeException(e);
        }
    }

    private Class<?> doGenerate(PojoDescriptor classDescription) throws NotFoundException, CannotCompileException {
        final String pojoFqn = classDescription.getPojoFqn();

        // 在缓存中查找
        final Class<?> cached = classCache.get(pojoFqn);
        if (cached != null) {
            return cached;
        }

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(pojoFqn);
        ClassFile classFile = cc.getClassFile();
        ConstPool constpool = classFile.getConstPool();

        cc.addInterface(resolveCtClass(Serializable.class));

        // 处理类级别元注释
        final List<AnnotationDescriptor> classLevelAnnotationDescriptors = classDescription.getClassLevelAnnotations();
        if (CollectionUtils.isNotEmpty(classLevelAnnotationDescriptors)) {

            for (AnnotationDescriptor ad : classLevelAnnotationDescriptors) {
                AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
                Annotation annotation = new Annotation(ad.getAnnotationFqn(), constpool);

                if (CollectionUtils.isNotEmpty(ad.getValues())) {
                    for (AnnotationDescriptor.AnnotationValue o : ad.getValues()) {
                        annotation.addMemberValue(o.getName(), AnnotationDescriptorHelper.createMemberValue(o, constpool));
                    }
                }
                annotationsAttribute.addAnnotation(annotation);
                classFile.addAttribute(annotationsAttribute);
            }

        }

        for (Map.Entry<String, Class<?>> entry : classDescription.entrySet()) {
            cc.addField(generateField(cc, entry.getKey(), entry.getValue(), constpool, findAnnotations(entry.getKey(), PojoDescriptor.AnnotationPos.FIELD, classDescription)));
            cc.addMethod(generateGetter(cc, entry.getKey(), entry.getValue(), constpool, findAnnotations(entry.getKey(), PojoDescriptor.AnnotationPos.GETTER, classDescription)));
            cc.addMethod(generateSetter(cc, entry.getKey(), entry.getValue(), constpool, findAnnotations(entry.getKey(), PojoDescriptor.AnnotationPos.SETTER, classDescription)));
        }

        Class<?> created = cc.toClass();
        this.classCache.put(pojoFqn, created);
        return created;
    }

    private CtField generateField(CtClass declaringClass, String fieldName, Class<?> fieldType, ConstPool constPool, AnnotationDescriptor... annotationDescriptors)
            throws NotFoundException, CannotCompileException {
        CtField ctField = new CtField(resolveCtClass(fieldType), fieldName, declaringClass);

        FieldInfo fieldInfo = ctField.getFieldInfo();
        for (AnnotationDescriptor ad : annotationDescriptors) {
            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation(ad.getAnnotationFqn(), constPool);

            if (CollectionUtils.isNotEmpty(ad.getValues())) {
                for (AnnotationDescriptor.AnnotationValue o : ad.getValues()) {
                    annotation.addMemberValue(o.getName(), AnnotationDescriptorHelper.createMemberValue(o, constPool));
                }
            }
            annotationsAttribute.addAnnotation(annotation);
            fieldInfo.addAttribute(annotationsAttribute);
        }

        return ctField;
    }

    private CtMethod generateGetter(CtClass declaringClass, String fieldName, Class<?> fieldClass, ConstPool constPool, AnnotationDescriptor... annotationDescriptors)
            throws CannotCompileException {

        String getterName = "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);

        StringBuffer sb = new StringBuffer();
        sb.append("public ").append(fieldClass.getName()).append(" ")
                .append(getterName).append("(){").append("return this.")
                .append(fieldName).append(";").append("}");
        CtMethod ctMethod = CtMethod.make(sb.toString(), declaringClass);

        // 生成元注释
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        for (AnnotationDescriptor ad : annotationDescriptors) {
            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation(ad.getAnnotationFqn(), constPool);

            if (CollectionUtils.isNotEmpty(ad.getValues())) {
                for (AnnotationDescriptor.AnnotationValue o : ad.getValues()) {
                    annotation.addMemberValue(o.getName(), AnnotationDescriptorHelper.createMemberValue(o, constPool));
                }
            }
            annotationsAttribute.addAnnotation(annotation);
            methodInfo.addAttribute(annotationsAttribute);
        }

        return ctMethod;
    }

    private CtMethod generateSetter(CtClass declaringClass, String fieldName, Class<?> fieldClass, ConstPool constPool, AnnotationDescriptor... annotationDescriptors)
            throws CannotCompileException {

        String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);

        StringBuffer sb = new StringBuffer();
        sb.append("public void ").append(setterName).append("(")
                .append(fieldClass.getName()).append(" ").append(fieldName)
                .append(")").append("{").append("this.").append(fieldName)
                .append("=").append(fieldName).append(";").append("}");
        CtMethod ctMethod = CtMethod.make(sb.toString(), declaringClass);

        // 生成元注释
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        for (AnnotationDescriptor ad : annotationDescriptors) {
            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation(ad.getAnnotationFqn(), constPool);

            if (CollectionUtils.isNotEmpty(ad.getValues())) {
                for (AnnotationDescriptor.AnnotationValue o : ad.getValues()) {
                    annotation.addMemberValue(o.getName(), AnnotationDescriptorHelper.createMemberValue(o, constPool));
                }
            }
            annotationsAttribute.addAnnotation(annotation);
            methodInfo.addAttribute(annotationsAttribute);
        }

        return ctMethod;
    }

    private CtClass resolveCtClass(Class<?> clazz) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(clazz.getName());
    }

    private AnnotationDescriptor[] findAnnotations(String property, PojoDescriptor.AnnotationPos pos, PojoDescriptor pojoDescriptor) {
        final List<AnnotationDescriptor> annotationDescriptors = new ArrayList<>();

        for (final Pair<PojoDescriptor.AnnotationPos, List<AnnotationDescriptor>> config : pojoDescriptor.getAllPropertyAnnotationConfig()) {
            if (config.getA() == pos) {
                CollectionUtils.nullSafeAddAll(annotationDescriptors, config.getB());
            }
        }

        for (final Tuple<String, PojoDescriptor.AnnotationPos, List<AnnotationDescriptor>> config : pojoDescriptor.getPropertyAnnotationConfig()) {
            if (config.getB() == pos && property.equals(config.getA())) {
                CollectionUtils.nullSafeAddAll(annotationDescriptors, config.getC());
            }
        }

        return annotationDescriptors.toArray(new AnnotationDescriptor[0]);
    }

}
