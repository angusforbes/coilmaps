package coilmaps;

/* CebProperties.java (created on Aug 4, 2008) */


import geometry.Colorf;
import java.util.HashMap;

/**
 * Contains various parameters for the different CEB visualizations that can be tuned
 * as needed. E.g., colors, sizes, etc.
 * 
 * @author Angus Forbes
 */
public class CebProperties
{
  //George's version 1
//    public static Colorf landColor = Colorf.newColorRGB(242, 241, 226);
//    public static Colorf seaColor = Colorf.newColorRGB(85,87, 87);
//    public static Colorf cityTextColor = Colorf.newColorRGB(252,127,2);
//  
  //George's version 2
//    public static Colorf landColor = Colorf.newColorRGB(217, 215, 197);
//    public static Colorf seaColor = Colorf.newColorRGB(215,198,195);
//    public static Colorf cityTextColor = Colorf.newColorRGB(218,141,41);
//  
  //opt 1
//  public static Colorf landColor = Colorf.newColorRGB(215,198,185);
//  public static Colorf seaColor = Colorf.newColorRGB(69,35,37);
//  public static Colorf cityTextColor = Colorf.newColorRGB(218,141,41);
  
  //opt 2
//  public static Colorf landColor = Colorf.newColorRGB(185,201,208);
//  public static Colorf seaColor = Colorf.newColorRGB(69,35,37);
//  public static Colorf cityTextColor = Colorf.newColorRGB(175,189,34);
//  
  //version 4
//  public static Colorf landColor = Colorf.newColorRGB(182,182,182);
//  public static Colorf seaColor = Colorf.newColorRGB(95,66,96);
//  public static Colorf cityTextColor = Colorf.newColorRGB(141,40,53);

    //version 3
//  public static Colorf landColor = Colorf.newColorRGB(202,176,79);
//  public static Colorf seaColor = Colorf.newColorRGB(126,126,126);
//  public static Colorf cityTextColor = Colorf.newColorRGB(141,40,53);
//  
  //version 2
//  public static Colorf seaColor = Colorf.newColorRGB(51,102,153);
//  public static Colorf landColor = Colorf.newColorRGB(126,126,126);
//  public static Colorf cityTextColor = Colorf.newColorRGB(141,40,53);

  //version 1
  public static Colorf seaColor; // = Colorf.newColorRGB(16,59,71);
  public static Colorf landColor; // = Colorf.newColorRGB(202, 176, 79);
  public static Colorf cityTextColor; // = Colorf.newColorRGB(53,3,53);
  public static Colorf cityColor; // = Colorf.newColorRGB(128,128,128);
  public static Colorf brightLandColor; // = Colorf.newColorRGB(128,128,128);

//Original   
//  public static Colorf seaColor = new Colorf(.2f, .2f, .5f, 1f);
//  public static Colorf landColor = Colorf.newColorRGB(100, 135, 125);
//  public static Colorf cityColor = Colorf.newColorRGB(255, 230, 3);
//  public static Colorf textColor = new Colorf(.1f, .1f, .1f, 1f);
  
  public static Colorf mainCityColor = new Colorf(.3f, .3f, .3f, 1f);
  public static Colorf eventColor = new Colorf(.9f, .8f, 0f, .5f);
  public static Colorf mainTextColor = new Colorf(1f, 1f, 1f, 1f);

  public static float eventCityRadius = .02f;
  public static float mainCityRadius = .04f;
  public static float mainCityTextHeight = .08f;  //GeoMap visualization...
  //public static Colorf cityColor = Colorf.newColorRGB(128,128,128);
  //public static Colorf cityColor = Colorf.newColorRGB(245, 128, 20);
  //public static Colorf cityColor = new Colorf(.3f, .3f, .3f);
  //public static Colorf cityTextColor = new Colorf(1f, .2f, .2f, 1f);
  //public static Colorf landColor = new Colorf(.55f, .75f, .65f, 1f);
  public static float CITY_SIZE; //= .04f;
  public static float CITY_TEXT_SIZE; // = .18f;

  //public static Colorf lineColor = new Colorf(.6f, .6f, .6f, 1f);
  public static Colorf lineColor = new Colorf(.8f, .8f, .8f, 1f);
  public static HashMap<String, String> programToPracticeMap = null;

  public static String getPracticeForProgram(String program)
  {
    if (programToPracticeMap == null)
    {
      initializeProgramToPracticeMap();
    }

    return programToPracticeMap.get(program);
  }

  public static Colorf getColorForPractice(String practice)
  {
    if (practice.equalsIgnoreCase("Sales & Marketing"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (practice.equalsIgnoreCase("Operations &  Procurement"))
    {
      return Colorf.newColorRGB(126, 155, 169);
    }
    if (practice.equalsIgnoreCase("Legal & Administration"))
    {
      return Colorf.newColorRGB(67, 182, 181);
    }
    if (practice.equalsIgnoreCase("Independent Programs"))
    {
      return Colorf.newColorRGB(150, 54, 117);
    }
    if (practice.equalsIgnoreCase("Future Practice"))
    {
      return Colorf.newColorRGB(233, 114, 162);
    }
    if (practice.equalsIgnoreCase("Information Technology"))
    {
      return Colorf.newColorRGB(199, 0, 9);
    }
    if (practice.equalsIgnoreCase("Human Resources"))
    {
      return Colorf.newColorRGB(255, 145, 5);
    }
    if (practice.equalsIgnoreCase("Corporate Finance"))
    {
      return Colorf.newColorRGB(255, 235, 26);
    }
    if (practice.equalsIgnoreCase("Financial Services"))
    {
      return Colorf.newColorRGB(98, 168, 39);
    }

    return Colorf.newColorRGB(50, 50, 50);
  }

  public static Colorf getColorForSector(String sector)
  {
    if (sector.toLowerCase().startsWith("diversified ser"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("telecommunicati"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("manufacturing"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("electronics"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("banking"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("transportation"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("computer softwa"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("government"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("consumer produc"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("aerospace"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("energy"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("insurance"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("health products"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    if (sector.toLowerCase().startsWith("computer softwa"))
    {
      return Colorf.newColorRGB(0, 129, 169);
    }
    
    return Colorf.newColorRGB(255,255,255);  
  }
  
  public static void initializeProgramToPracticeMap()
  {
    programToPracticeMap = new HashMap<String, String>();

    programToPracticeMap.put("division finance forum", "corporate finance");
    programToPracticeMap.put("investor relations roundtable", "corporate finance");
    programToPracticeMap.put("audit reference center", "corporate finance");
    programToPracticeMap.put("finance leadership academy", "corporate finance");
    programToPracticeMap.put("treasury leadership roundtable", "corporate finance");
    programToPracticeMap.put("controllers' leadership roundtable", "corporate finance");
    programToPracticeMap.put("tax director roundtable", "corporate finance");
    programToPracticeMap.put("cfo executive board", "corporate finance");
    programToPracticeMap.put("government finance roundtable", "corporate finance");
    programToPracticeMap.put("audit director roundtable", "corporate finance");
    programToPracticeMap.put("operations council", "financial services");
    programToPracticeMap.put("business banking board", "financial services");
    programToPracticeMap.put("financial services ceo council", "financial services");
    programToPracticeMap.put("retirement services roundtable", "financial services");
    programToPracticeMap.put("insurance advisory board", "financial services");
    programToPracticeMap.put("vip forum", "financial services");
    programToPracticeMap.put("council on financial competition", "financial services");
    programToPracticeMap.put("mortgage executive roundtable", "financial services");
    programToPracticeMap.put("investment management executive council", "financial services");
    programToPracticeMap.put("corporate leadership council", "human resources");
    programToPracticeMap.put("china hr executive board", "human resources");
    programToPracticeMap.put("benefits roundtable", "human resources");
    programToPracticeMap.put("learning and development roundtable", "human resources");
    programToPracticeMap.put("clc hr leadership academy", "human resources");
    programToPracticeMap.put("compensation roundtable", "human resources");
    programToPracticeMap.put("recruiting roundtable", "human resources");
    programToPracticeMap.put("mlc markplan", "independent programs");
    programToPracticeMap.put("manager excellence resource center", "independent programs");
    programToPracticeMap.put("corporate strategy board", "independent programs");
    programToPracticeMap.put("liquidity edge", "independent programs");
    programToPracticeMap.put("information risk executive council", "information technology");
    programToPracticeMap.put("applications executive council", "information technology");
    programToPracticeMap.put("cio executive board", "information technology");
    programToPracticeMap.put("infrastructure executive council", "information technology");
    programToPracticeMap.put("it gold portal", "information technology");
    programToPracticeMap.put("enterprise architecture executive council", "information technology");
    programToPracticeMap.put("infrastructure performance improvement lab", "information technology");
    programToPracticeMap.put("it platinum portal", "information technology");
    programToPracticeMap.put("pmo executive council", "information technology");
    programToPracticeMap.put("data center operations council", "information technology");
    programToPracticeMap.put("it business leadership academy", "information technology");
    programToPracticeMap.put("compliance & ethics leadership council", "legal & administration");
    programToPracticeMap.put("audit committee leadership forum", "legal & administration");
    programToPracticeMap.put("general counsel roundtable", "legal & administration");
    programToPracticeMap.put("compliance peak", "legal & administration");
    programToPracticeMap.put("logistics leadership board", "operations & procurement");
    programToPracticeMap.put("quality executive board", "operations & procurement");
    programToPracticeMap.put("supply chain executive board", "operations & procurement");
    programToPracticeMap.put("manufacturing leadership board", "operations & procurement");
    programToPracticeMap.put("procurement strategy council", "operations & procurement");
    programToPracticeMap.put("real estate executive board", "operations & procurement");
    programToPracticeMap.put("research & technology executive council", "operations & procurement");
    programToPracticeMap.put("customer contact council", "sales & marketing");
    programToPracticeMap.put("business leadership forum", "sales & marketing");
    programToPracticeMap.put("marketing excellence survey", "sales & marketing");
    programToPracticeMap.put("sales executive council", "sales & marketing");
    programToPracticeMap.put("sales operations excellence center", "sales & marketing");
    programToPracticeMap.put("inside sales roundtable", "sales & marketing");
    programToPracticeMap.put("marketing leadership council", "sales & marketing");
    programToPracticeMap.put("advertising and marketing communications roundtable", "sales & marketing");
    programToPracticeMap.put("market research executive board", "sales & marketing");
    programToPracticeMap.put("communications executive council", "sales & marketing");
  }
}
