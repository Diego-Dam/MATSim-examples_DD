/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.project;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.*;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nagel
 *
 */
public class RunMatsim{

	public static void main(String[] args) {

		Config config;
		if ( args==null || args.length==0 || args[0]==null ){
			config = ConfigUtils.loadConfig( "scenarios/equil/config.xml" );
		} else {
			config = ConfigUtils.loadConfig( args );
		}
		config.controler().setOverwriteFileSetting(
				OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );

		// possibly modify config here
		
		// ---
		
		Scenario scenario = ScenarioUtils.loadScenario(config) ;

		// possibly modify scenario here > delete all persons from the population except for Id1
		/** Id<Link> interestingLinkId = Id.createLinkId(6);
		List<Id<Link>> linksToModify = new ArrayList<>();
		for (Id<Person> linkId :scenario.getNetwork().getLinks().keySet() {
			if (!linkId.equals(interestingLinkId)) {
 				linksToModify.add(linkId);
			}
		}

		for (Id<Link> linkId : linksToRemove) {
			scenario.getNetwork().setCapacityPeriod() ;
		} **/

		// possibly modify scenario here > delete all persons from the population except for Id1
		Id<Person> interestingPersonId = Id.createPersonId(1);
		List<Id<Person>> personsToRemove = new ArrayList<>();
		for (Id<Person> personId :scenario.getPopulation().getPersons().keySet()) {
			if (!personId.equals(interestingPersonId)) {
				personsToRemove.add(personId);
			}
		}

		for (Id<Person> personId : personsToRemove) {
			scenario.getPopulation().removePerson(personId);
		}

		System.out.println("Population size = " + scenario.getPopulation().getPersons().size());

		// create population with an activity plan and add it to the simulation
		// create  person 2 going out at 6 in the morning
		PopulationFactory populationFactory = scenario.getPopulation().getFactory();
		Person person2 = populationFactory.createPerson(Id.createPersonId("Diego"));

		Plan plan2 = populationFactory.createPlan();

		Activity homeActivity = populationFactory.createActivityFromLinkId( "h", Id.createLinkId(21));
		homeActivity.setEndTime(6*60*60.);
		plan2.addActivity(homeActivity);

		Leg leg = populationFactory.createLeg(TransportMode.car);
		plan2.addLeg(leg);

		Activity workActivity = populationFactory.createActivityFromLinkId( "w", Id.createLinkId(1));
		plan2.addActivity(workActivity);

		person2.addPlan(plan2);

		scenario.getPopulation().addPerson(person2);

		System.out.println("Population size = " + scenario.getPopulation().getPersons().size());

		/* create person 3 */

		Person person3 = populationFactory.createPerson(Id.createPersonId("Johnny"));

		Plan plan3 = populationFactory.createPlan();

		Activity homeActivity3 = populationFactory.createActivityFromLinkId( "h", Id.createLinkId(23));
		homeActivity3.setEndTime(6.5*60*60.);
		plan3.addActivity(homeActivity3);

		Leg leg3 = populationFactory.createLeg(TransportMode.car);
		plan3.addLeg(leg3);

		Activity workActivity3 = populationFactory.createActivityFromLinkId( "w", Id.createLinkId(7));
		plan3.addActivity(workActivity3);

		person3.addPlan(plan3);

		scenario.getPopulation().addPerson(person3);

		System.out.println("Population size = " + scenario.getPopulation().getPersons().size());

		/* create person 4 */

		Person person4 = populationFactory.createPerson(Id.createPersonId("Tony"));

		Plan plan4 = populationFactory.createPlan();

		Activity homeActivity4 = populationFactory.createActivityFromLinkId( "h", Id.createLinkId(23));
		homeActivity4.setEndTime(6.5*60*60.);
		plan4.addActivity(homeActivity4);

		Leg leg4 = populationFactory.createLeg(TransportMode.car);
		plan4.addLeg(leg4);

		Activity workActivity4 = populationFactory.createActivityFromLinkId( "w", Id.createLinkId(8));
		plan4.addActivity(workActivity4);

		person4.addPlan(plan4);

		scenario.getPopulation().addPerson(person4);

		System.out.println("Population size = " + scenario.getPopulation().getPersons().size());

		Controler controler = new Controler( scenario ) ;
		controler.addOverridingModule( new OTFVisLiveModule() ) ;
		controler.run();
		// possibly modify controler here

//		controler.addOverridingModule( new OTFVisLiveModule() ) ;
		
		// ---
		
		controler.run();
	}
	
}
