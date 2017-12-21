package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class NewProblemCall implements Serializable
{
    private int result;
    
    private List<NewProblemBean> problems;

    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public List<NewProblemBean> getProblems()
    {
        return problems;
    }

    public void setProblems(List<NewProblemBean> problems)
    {
        this.problems = problems;
    }
   
    
}
