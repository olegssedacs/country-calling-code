package com.neotech.ccc.domain.model.entities;

import lombok.NonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

/**
 * Immutable, Thread-safe
 * B-Tree-Like structure of country calling codes
 * Structure :
 * HashMap of TreeMap
 * "1" -> TreeMap "1xx" codes
 * "2" -> TreeMap "2xx" codes
 * ...
 * "9" -> TreeMap "9xx" codes
 */
public class CountryCallingCodesMap {

    private final Map<Character, Map<String, CountryCallingCode>> indexedMaps;
    private int size = 0;

    public CountryCallingCodesMap(Collection<CountryCallingCode> codes) {
        this.indexedMaps = new HashMap<>();
        codes.forEach(this::add);
    }

    public int size() {
        return size;
    }

    public Optional<CountryCallingCode> get(@NonNull PhoneNumber phoneNumber) {
        var phone = phoneNumber.getValue();
        return indexedMaps.getOrDefault(phone.charAt(0), Map.of())
                          .entrySet()
                          .stream()
                          .filter(entry -> phone.startsWith(entry.getKey()))
                          .findFirst()
                          .map(Map.Entry::getValue)
                          .map(this::copyValue);
    }

    private void add(@NonNull CountryCallingCode callingCode) {
        var code = String.valueOf(callingCode.getCallingCode());
        var tree = getOrCreateNestedTree(code.charAt(0));
        var oldValue = tree.put(code, callingCode);
        if (oldValue == null) {
            this.size++;
        }
    }

    private Map<String, CountryCallingCode> getOrCreateNestedTree(char firstDigit) {
        if (indexedMaps.containsKey(firstDigit)) {
            return indexedMaps.get(firstDigit);
        }
        indexedMaps.put(firstDigit, new TreeMap<>(new CodeComparator().reversed()));
        return indexedMaps.get(firstDigit);
    }

    private CountryCallingCode copyValue(CountryCallingCode code) {
        var countries = code.getCountries()
                            .stream()
                            .map(country -> new Country(country.getName(), country.getAlpha2Code()))
                            .collect(toList());
        return new CountryCallingCode(code.getCallingCode(), countries);
    }

    private static class CodeComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            int result = Integer.compare(o1.length(), o2.length());
            return result != 0
                   ? result
                   : o1.compareTo(o2);
        }
    }
}
