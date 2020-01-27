# Maps to robustness elements
nouns -> entity obj, boundary obj  
verbs -> controller, connects nouns  
user, external system -> actor  

# Rules
**noun-verb**: entity - controller, boundary - controller  
**verb-verb**: controller - controller  
**actor-boundary**  
**noun-noun**: should not ever happen  

# Elements of a Robustness diagram

### Example
`Customer` (Actor) --- `Book detail page` (Boundary)  
`Book detail page` --> (click write review button) `display` (Controller)  
`display` --> `Write review page` (Boundary)  
`Customer` (Actor) --- (enter review and click button) `Write review page`  
