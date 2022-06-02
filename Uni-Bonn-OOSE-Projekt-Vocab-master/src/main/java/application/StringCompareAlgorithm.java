package application;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StringCompareAlgorithm {

    public static Cost calculate(String input, String output, boolean ignoreCase) {
        if (input.isEmpty() && output.isEmpty()) {
            return new Cost();
        } else if (input.isEmpty()) {
            return new Cost(IntStream.range(0, output.length())
                    .mapToObj(position -> ModificationType.INSERTION.newModification(0))
                    .collect(Collectors.toList()));
        } else if (output.isEmpty()) {
            return new Cost(IntStream.range(0, input.length())
                    .mapToObj(ModificationType.DELETION::newModification)
                    .collect(Collectors.toList()));
        }

        boolean charEquals = ignoreCase ?
                input.substring(0, 1).equalsIgnoreCase(output.substring(0, 1)) :
                input.charAt(0) == output.charAt(0);

        Cost additionalCost;
        if (charEquals)
            additionalCost = new Cost();
        else
            additionalCost = ModificationType.SUBSTITUTION.newCost(0);

        Cost substitutionCost = Cost.merge(
                additionalCost,
                calculate(input.substring(1), output.substring(1), ignoreCase)
                        .shiftPosition(1)
        );
        Cost insertionCost = Cost.merge(
                ModificationType.INSERTION.newCost(0),
                calculate(input, output.substring(1), ignoreCase)
        );
        Cost deletionCost = Cost.merge(
                ModificationType.DELETION.newCost(0),
                calculate(input.substring(1), output, ignoreCase)
                        .shiftPosition(1)
        );

        return Stream.of(substitutionCost, insertionCost, deletionCost)
                .min(Comparator.comparingInt(Cost::getCost))
                .orElse(new Cost());
    }

    public static class Cost {
        private final List<Modification> modifications;

        public Cost() {
            this.modifications = List.of();
        }

        public Cost(Modification... modifications) {
            this.modifications = List.of(modifications);
        }

        public Cost(Collection<Modification> modifications) {
            this.modifications = List.copyOf(modifications);
        }

        public Cost shiftPosition(int amount) {
            return new Cost(
                    modifications.stream()
                            .map(modification -> new Modification(modification.getType(), modification.getPosition() + amount))
                            .collect(Collectors.toList())
            );
        }

        public int getCost() {
            return modifications.size();
        }

        public List<Modification> getModifications() {
            return modifications;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Cost.class.getSimpleName() + "[", "]")
                    .add("modifications=" + modifications)
                    .toString();
        }

        public static Cost merge(Cost cost1, Cost cost2) {
            return new Cost(Stream.concat(cost1.getModifications().stream(), cost2.getModifications().stream())
                    .collect(Collectors.toList()));
        }
    }

    public static class Modification {
        private final ModificationType type;
        private final int position;

        public Modification(ModificationType type, int position) {
            this.type = Objects.requireNonNull(type);
            this.position = position;
        }

        public ModificationType getType() {
            return type;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Modification.class.getSimpleName() + "[", "]")
                    .add("type=" + type)
                    .add("position=" + position)
                    .toString();
        }
    }

    public enum ModificationType {
        SUBSTITUTION,
        INSERTION,
        DELETION;

        public Modification newModification(int position) {
            return new Modification(this, position);
        }

        public Cost newCost(int position) {
            return new Cost(newModification(position));
        }
    }
}