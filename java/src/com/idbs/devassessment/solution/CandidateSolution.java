/*
 * Copyright (C) 1993-2020 ID Business Solutions Limited
 * All rights reserved
 */

/*
 * The 3 levels has been attempted but the third level was heavy on CPU
 * and takes a little time to complete causing me to comment the code without the use of any operators out
 * Due to my school assignments and coursework i could not complete any tests
 */
package com.idbs.devassessment.solution;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;


import com.idbs.devassessment.core.IDBSSolutionException;
import com.idbs.devassessment.core.DifficultyLevel;
import com.idbs.devassessment.harness.DigitalTaxTracker;


/**
 * Example solution for the example question
 */

public class CandidateSolution extends AssessmentSolutionBase
{
    @Override
    public DifficultyLevel getDifficultyLevel()
    {
        /*
         *
         * CHANGE this return type to YOUR selected choice of difficulty level to which you will code an answer to.
         *
         */
        return DifficultyLevel.LEVEL_3;
    }



    @Override
    public String getAnswer() throws IDBSSolutionException
    {

        //Declare a string that returns the value of the answer
        String MainAnswer = "";

        // first get Json as a String for the question using the inherited method...
        String QuestionData = getDataForQuestion();

        //differentiate between data by checking if it contains numeric
        if (QuestionData.contains("numeric"))
        {
            MainAnswer = SplitHelperMethod(QuestionData);
        }else{
            // if not then its json (can still be optimized to take other data types)
            MainAnswer = jsonDataSolution(QuestionData);
        }

        return MainAnswer;
    }


    /*
     *
     * This method breakdown the json and returns the values using a jsonHelper method
     *
     */
    private String jsonDataSolution(String QData)
    {
        JsonReader reader = Json.createReader(new StringReader(QData));
        JsonObject jsonObject = reader.readObject();
        reader.close();

        //extracts their values fromm the Json
        int startValue = jsonObject.getInt("xValue");
        JsonArray jsonArray = jsonObject.getJsonArray("terms");

        //declare a variable to hold the answer
        long answer = 0;

        //Run a loop to iterate across the json object and perform calculations
        for (int i = 0; i < jsonArray.size(); i++)
        {
            //calculates the sum of answers on every iteration in addition to value of result from helper method jsonHelper
            answer = DigitalTaxTracker.add(answer,jsonHelper(startValue, jsonArray.getJsonObject(i)));
        }

        //returns the answer in string
        return Long.toString(answer);
    }



    /*
     *
     * This helper method is used to get individual values of json
     *
     */
    private long jsonHelper(int xValue, JsonObject objectValues)
    {
        int power = objectValues.getInt("power");
        int multiplier = objectValues.getInt("multiplier");
        int operator = -1;
        String action = objectValues.getString("action");

        if (Objects.equals(action, "add"))
        {
            operator = 1;
        }
         long ans = multiplierMethod(operator,multiplier) * powerOfMethod(xValue, power);
//         long ans = multiplierMethod(multiplierMethod(operator,multiplier), powerOfMethod(xValue, power));

        return ans;
    }


    /*
     *
     * This method takes the data and Returns the disintegrated solved answer in string
     *
     */
    private String SplitHelperMethod(String qData)
    {
        //splits the string into 2 by using the semi-column
        String[] value = qData.split(";",2);

        //remove white space from both ends
        String Xvalue = value[0].trim();

        //Gets everything from y =
        String Yvalue = value[1].trim();

        //Gets the value of the x by getting value after =
        String[] intXval = Xvalue.split("=",2);

        //Gets the value of the y by getting values after =
        String[] intYval = Yvalue.split("=",2);

        //converts value of x to integer
        long NeededXvalue = Integer.parseInt(intXval[1].trim());

        //holds the values in y
        String NeededYvalue = intYval[1].trim();

        //declared a variable to hold the final answer
        long answer = 0;

        //splits the strings by their operators to separate them individually
        String[] SplitString = NeededYvalue.split("(?=[+-])");

        //loop to reassign new values
        for (int i = 0; i < SplitString.length; i++)
        {
            String action = Character.toString(SplitString[i].charAt(0));
            int multiplier =  Integer.parseInt(SplitString[i].split("\\.")[0].substring(1));
            int coefficent = Integer.parseInt(SplitString[i].split("\\^")[1]);
            String XString = SplitString[i].split("\\^")[0].split("\\.")[1];
            int operator = -1;

            //checks if negative or negative
            if (action.equals("+"))
            {
                operator = 1;
            }

            //sums result of operation to answer variable
            answer += multiplierMethod(operator,multiplier) * powerOfMethod(NeededXvalue,coefficent);
//          answer += multiplierMethod(multiplierMethod(operator,multiplier), powerOfMethod(NeededXvalue,coefficent));

        }
        //coverts to string return the result of the method
        return Long.toString(answer);
    }



    /*
     *
     * Custom Method that gets the product of coefficient by exponent times
     *
     */
    private Map<String, Long> powerCache = new HashMap<>();

    private long powerOfMethod(long value, long power) {
        String cacheKey = value + "," + power;
        if (powerCache.containsKey(cacheKey)) {
            return powerCache.get(cacheKey);
        } else {
            long result = 1;
            while (power > 0) {
                result = multiplierMethod(result, value);
                power--;
            }
            powerCache.put(cacheKey, result);
            return result;
        }
    }

    /*
     *
     * Custom Multiplier method
     *
     */
    private long multiplierMethod(long a, long b)
    {
        long result = 0;
        for (int i = 0; i < b; i++)
        {
            result = DigitalTaxTracker.add(result,a);
        }
        return result;
    }

}


