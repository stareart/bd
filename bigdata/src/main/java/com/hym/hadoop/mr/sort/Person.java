package com.hym.hadoop.mr.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Person implements WritableComparable<Person> {
    private String name;
    private int age;
    private int salary;

    public Person() {
    }

    public Person(String name, int age, int salary) {
        //super();
        this.name = name;
        this.age = age;
        this.salary = salary;
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

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return this.salary + "  " + this.age + "    " + this.name;
    }

    //先比较salary，高的排序在前；若相同，age小的在前
    public int compareTo(Person o) {
        int compareResult1= this.salary - o.salary;
        if(compareResult1 != 0) {
            return -compareResult1;
        } else {
            return this.age - o.age;
        }
    }

    //序列化，将NewKey转化成使用流传送的二进制
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(name);
        dataOutput.writeInt(age);
        dataOutput.writeInt(salary);
    }

    //使用in读字段的顺序，要与write方法中写的顺序保持一致
    public void readFields(DataInput dataInput) throws IOException {
        //read string
        this.name = dataInput.readUTF();
        this.age = dataInput.readInt();
        this.salary = dataInput.readInt();
    }
}