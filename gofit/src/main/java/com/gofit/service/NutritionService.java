package com.gofit.service;

import com.gofit.model.Ingredient;
import com.gofit.model.Meal;
import com.gofit.model.User;
import com.gofit.model.Goal;
import com.gofit.repository.IngredientRepository;
import com.gofit.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class NutritionService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private MealRepository mealRepository;

    private final Random random = new Random();

    public void generatePlanForUser(User user) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        // Luăm caloriile zilnice ale utilizatorului
        int dailyTarget = user.getDailyCalories();

        // Curățăm string-ul pentru a se potrivi cu Enum-ul Goal
        Goal userGoal = user.getGoal();

        for (String day : days) {
            // Generăm un factor de variație pentru a nu avea calorii identice zilnic
            // Acest random adaugă o variație de +/- 10% per masă pentru un aspect mai natural

            // 1. Breakfast (~20%)
            double v1 = 0.9 + (random.nextDouble() * 0.2);
            saveGeneratedMeal(day, "Breakfast", (int)(dailyTarget * 0.20 * v1), userGoal);

            // 2. Snack 1 (~10%)
            double v2 = 0.9 + (random.nextDouble() * 0.2);
            saveGeneratedMeal(day, "Snack 1", (int)(dailyTarget * 0.10 * v2), userGoal);

            // 3. Lunch (~35%)
            double v3 = 0.9 + (random.nextDouble() * 0.2);
            saveGeneratedMeal(day, "Lunch", (int)(dailyTarget * 0.35 * v3), userGoal);

            // 4. Snack 2 (~15%)
            double v4 = 0.9 + (random.nextDouble() * 0.2);
            saveGeneratedMeal(day, "Snack 2", (int)(dailyTarget * 0.15 * v4), userGoal);

            // 5. Dinner (~20%)
            double v5 = 0.9 + (random.nextDouble() * 0.2);
            saveGeneratedMeal(day, "Dinner", (int)(dailyTarget * 0.20 * v5), userGoal);
        }
    }

    private void saveGeneratedMeal(String day, String type, int targetCals, Goal goal) {
        List<String> targets = List.of(goal.name(), "MAINTAIN");
        List<Ingredient> baseList, sideList;

        if (type.equals("Breakfast")) {
            baseList = ingredientRepository.findByCategoryAndSuitabilityIn("BREAKFAST_BASE", targets);
            sideList = ingredientRepository.findByCategoryAndSuitabilityIn("BREAKFAST_TOPPING", targets);
        } else if (type.contains("Snack")) {
            // Pentru gustări folosim doar categoria SNACK
            baseList = ingredientRepository.findByCategoryAndSuitabilityIn("SNACK", targets);
            sideList = baseList; // Gustările pot fi un singur ingredient sau combinații
        } else {
            baseList = ingredientRepository.findByCategoryAndSuitabilityIn("PROTEIN", targets);
            sideList = ingredientRepository.findByCategoryAndSuitabilityIn("VEGGIE", targets);
            if (sideList.isEmpty()) sideList = ingredientRepository.findByCategoryAndSuitabilityIn("CARB", targets);
        }

        if (baseList.isEmpty() || sideList.isEmpty()) return;

        // Filtrare strictă (logica ta existentă)
        List<Ingredient> strictBases = baseList.stream().filter(i -> i.getSuitability().equals(goal.name())).toList();
        Ingredient base = !strictBases.isEmpty() ? strictBases.get(random.nextInt(strictBases.size())) : baseList.get(random.nextInt(baseList.size()));

        List<Ingredient> strictSides = sideList.stream().filter(i -> i.getSuitability().equals(goal.name())).toList();
        Ingredient side = !strictSides.isEmpty() ? strictSides.get(random.nextInt(strictSides.size())) : sideList.get(random.nextInt(sideList.size()));

        // REPARARE GRAMAJ BROCCOLI:
        // Dacă e Lunch/Dinner, alocăm 70% calorii proteinei și doar 30% garniturii (legume/carbi)
        double ratioBase = type.contains("Snack") ? 0.5 : 0.7;
        double ratioSide = 1.0 - ratioBase;

        // 1. Calculăm gramajele brute (raw)
        double rawProtGrams = (targetCals * ratioBase * 100) / base.getCaloriesPer100g();
        double rawSideGrams = (targetCals * ratioSide * 100) / side.getCaloriesPer100g();

        // 2. Rotunjim gramajele la multiplu de 10 (ex: 184g devine 180g) pentru un aspect natural
        int protGrams = (int) (Math.round(rawProtGrams / 10.0) * 10);
        int sideGrams = (int) (Math.round(rawSideGrams / 10.0) * 10);

        // 3. RECALCULĂM caloriile REALE pe baza gramajelor rotunjite
        // Aceasta este linia care face ca numerele să fie diferite în UI!
        int actualCalories = (int) ((protGrams * base.getCaloriesPer100g() + (base.equals(side) ? 0 : sideGrams * side.getCaloriesPer100g())) / 100);

        Meal meal = new Meal();
        meal.setDayOfWeek(day);
        meal.setMealType(type);
        meal.setName(base.getName() + (base.equals(side) ? "" : " & " + side.getName()));

        // Folosim noile variabile int (protGrams, sideGrams)
        meal.setIngredients(String.format("%dg %s" + (base.equals(side) ? "" : ", %dg %s"), protGrams, base.getName(), sideGrams, side.getName()));

        // IMPORTANT: Salvăm caloriile REALE, nu pe cele țintă
        meal.setCalories(actualCalories);
        meal.setTargetGoal(goal);

        // Calculăm Macros folosind gramajele rotunjite
        meal.setProtein((int)((protGrams * base.getProteinPer100g() + (base.equals(side) ? 0 : sideGrams * side.getProteinPer100g()))/100));
        meal.setCarbs((int)((protGrams * base.getCarbsPer100g() + (base.equals(side) ? 0 : sideGrams * side.getCarbsPer100g()))/100));
        meal.setFats((int)((protGrams * base.getFatsPer100g() + (base.equals(side) ? 0 : sideGrams * side.getFatsPer100g()))/100));

        mealRepository.save(meal);
    }
}