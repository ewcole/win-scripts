@echo off
:: Look up a Latin word in the Perseus analytical lexicon
for %%f in (%*) do (
    start http://www.perseus.tufts.edu/hopper/morph?la=la^&l=%%f
)
