@echo off
:: Look up a Greek word in the Perseus analytical lexicon 
for %%f in (%*) do (
    start http://www.perseus.tufts.edu/hopper/morph?la=greek^&l=%%f
)
