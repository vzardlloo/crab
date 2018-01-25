package crab.config;


import crab.Crab;

public interface CrabConfiguration {

    public String getValue(String key);

    public Integer getIntValue(String key);

    public Long getLongValue(String key);

    public Double getDoubleValue(String key);


}
