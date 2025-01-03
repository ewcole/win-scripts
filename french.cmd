@echo off
:: Look up a French word
for %%f in (%*) do (
    start "" "https://www.collinsdictionary.com/dictionary/french-english/%%f"
)
