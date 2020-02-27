%Program: COSC_3410_Assignment_6_Problem_2
%Version: 1.0
%Author: David Helminiak
%Date Created: 1 November 2018
%Date Last Modified: 7 November 2018

option1 :- 
	classes(List),
	member(class(_, room_310, _, _, _), List),
	member(class(_, room_001, _, _, _), List),
	member(class(_, room_112, _, _, _), List),
	member(class(_, room_126, _, _, _), List),
	member(class(_, _, artificial_intelligence, _, _), List),
	member(class(_, _, hardware, _, _), List),
	member(class(_, _, compilers, _, _), List),
	member(class(_, _, operating_systems, _, _), List),
	member(class(_, _, _, ms_saunders, _), List),
	member(class(_, _, _, mr_shariar, _), List),
	member(class(_, _, _, mr_adams, _), List),
	member(class(_, _, _, ms_cheng, _), List),
	member(class(_, _, _, _, video_projector), List),
	member(class(_, _, _, _, clickers), List),
	member(class(_, _, _, _, whiteboard), List),
	member(class(_, _, _, _, podium), List),

	%Classes are in order from 9 to 12
	List = [class(9, _, _, _, _), class(10, _, _, _, _), class(11, _, _, _, _), class(12, _, _, _, _)],

	%Artificial Intelligence is taught in Room 310.
	member(class(_, room_310, artificial_intelligence, _, _), List),

	%The Hardware teacher uses a video projector.
	member(class(_, _, hardware, _, video_projector), List),

	%Ms. Saunders teaches in Room 001.
	member(class(_, room_001, _, ms_saunders, _), List),

	%The course in Room 001 uses clickers.
	member(class(_, room_001, _, _, clickers), List),

	%Mr. Shariar uses the whiteboard.
	member(class(_, _, _, mr_shariar, whiteboard), List),

	%Ms. Cheng teaches the hour before Operating Systems is taught.
	class_before(class(_, _, _, ms_cheng, _), class(_, _, operating_systems, _, _), List),

	%Room 112 holds a class at 11:00AM.
	member(class(11, room_112, _, _, _), List),

	%Mr. Adams teaches in Room 126.
	member(class(_, room_126, _, mr_adams, _), List),

	%A podium is used in the class at 9:00AM.
	member(class(9, _, _, _, podium), List),
	
	%The Compilers class meets in the hour before the class in Room 126.
	class_before(class(_, _, compilers, _, _), class(_, room_126, _, _, _), List),

	print_classes(List).

% 4 classes with times from 9 to 12, classroom, subject, teacher, presentation method
classes([
	class(_, _, _, _, _), 
	class(_, _, _, _, _), 
	class(_, _, _, _, _), 
	class(_, _, _, _, _)
]).

%Class A is positioned immediately to the left of B
class_before(A, B, [A, B | _]).
class_before(A, B, [_ | Y]) :- class_before(A, B, Y).

print_classes([A|B]) :- !,
	write(A), nl,
	print_classes(B).
print_classes([]).

:- initialization(option1).