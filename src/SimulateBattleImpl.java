import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> allUnits = new ArrayList<>(playerArmy.getUnits());
        allUnits.addAll(computerArmy.getUnits());

        while (isArmyAlive(playerArmy) && isArmyAlive(computerArmy)) {
            // Получаем список живых юнитов
            List<Unit> activeUnits = getLivingUnits(allUnits);

            // Сортируем по убыванию атаки
            activeUnits.sort((u1, u2) ->
                    Integer.compare(u2.getBaseAttack(), u1.getBaseAttack())
            );

            for (Unit attacker : activeUnits) {
                if (!attacker.isAlive()) {
                    continue;
                }

                // Получаем цель для атаки
                Unit target = attacker.getProgram().attack();

                if (target == null || !target.isAlive()) {
                    continue;
                }

                // Выполняем атаку
                attacker.attack(target);

                // Логируем действие
                printBattleLog.printBattleLog(attacker, target);

                // Если цель погибла, удаляем её из соответствующей армии
                if (!target.isAlive()) {
                    if (target.getArmy() == playerArmy) {
                        playerArmy.getUnits().remove(target);
                    } else {
                        computerArmy.getUnits().remove(target);
                    }
                }

                // Пауза для визуализации хода
                Thread.sleep(500);
            }
        }

        // Определяем победителя
        if (isArmyAlive(playerArmy)) {
            System.out.println("Армия игрока победила!");
        } else {
            System.out.println("Армия компьютера победила!");
        }
    }

    /**
     * Проверяет, есть ли в армии живые юниты
     */
    private boolean isArmyAlive(Army army) {
        for (Unit unit : army.getUnits()) {
            if (unit.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает список живых юнитов из общего списка
     */
    private List<Unit> getLivingUnits(List<Unit> units) {
        List<Unit> livingUnits = new ArrayList<>();
        for (Unit unit : units) {
            if (unit.isAlive()) {
                livingUnits.add(unit);
            }
        }
        return livingUnits;
    }
}