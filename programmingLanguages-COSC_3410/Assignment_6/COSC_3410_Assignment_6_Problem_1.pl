%Program: COSC_3410_Assignment_6_Problem_1
%Version: 1.0
%Author: David Helminiak
%Date Created: 1 November 2018
%Date Last Modified: 7 November 2018

option1 :- 
	preferences(List),
	member(preference(_, pizza), List),
	member(preference(_, burgers), List),
	member(preference(_, salads), List),
	member(preference(_, tacos), List),

	%Naomi doesn't like either pizza or burgers.
	(member(preference(naomi, salads), List); member(preference(naomi, tacos), List)),

	%Salim won't eat salads.
	(member(preference(salim, pizza), List); member(preference(salim, burgers), List); member(preference(salim, tacos), List)), 

	%Edward wants tacos.
	member(preference(edward, tacos), List),
	
	%Joan isn't fond of pizza.
	(member(preference(joan, burgers), List); member(preference(joan, salads), List); member(preference(joan, tacos), List)), 

	%Print preferences
	print_preferences(List).

preferences([
	preference(naomi, _), 
	preference(salim, _), 
	preference(edward, _), 
	preference(joan, _)
]).

print_preferences([A|B]) :- !,
	write(A), nl,
	print_preferences(B).
print_preferences([]).

:- initialization(option1).