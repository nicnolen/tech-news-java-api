package com.technews.testModel;

import java.util.Objects;

public class Demo {
    private String name;

    public Demo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Spring Data JPA uses the equal() method to compare 2 objects by hash code (reference number) instead of value, so we override equals() and hashCode() to compare objects based on values
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Demo demo = (Demo) o;
        return age == demo.age && Objects.equals(name, demo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    // override toString() to return the value of an object instead of reference number
    @Override
    public String toString() {
        return "Demo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int age;
}
