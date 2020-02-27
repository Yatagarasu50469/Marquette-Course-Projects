%Name: helminiak_assignment9
%Author: David Helminiak
%Date Created: 7 December 2018
%Date Modified: 7 December 2018

%SAMPLE PROGRAM
%===============================================================
%c(helminiak_assignment9).
%f(PID).
%PID = helminiak_assignment9:start().
%helminiak_assignment9:show_shelves(PID).
%helminiak_assignment9:add_item(PID, milk, 2.01).
%helminiak_assignment9:add_item(PID, lettuce, 0).
%helminiak_assignment9:show_shelves(PID).
%helminiak_assignment9:choose(PID, cupcake, 100).
%helminiak_assignment9:choose(PID, milk, 2).
%helminiak_assignment9:choose(PID, lettuce, 1).
%helminiak_assignment9:show_basket(PID).
%helminiak_assignment9:remove(PID, lettuce).
%helminiak_assignment9:show_basket(PID).
%helminiak_assignment9:checkout(PID).
%init:stop().

%===============================================================
%SAMPLE RUN
%===============================================================
%1> c(helminiak_assignment9).
%helminiak_assignment9.erl:37: Warning: variable 'Name' is unused
%helminiak_assignment9.erl:38: Warning: variable 'Remain' is unused
%helminiak_assignment9.erl:39: Warning: variable 'First' is unused
%{ok,helminiak_assignment9}
%2> f(PID).
%ok
%3> PID = helminiak_assignment9:start().
%<0.85.0>
%4> helminiak_assignment9:show_shelves(PID).
%[{cupcake,0.75},{lettuce,1.5},{soup,2.35}]
%5> helminiak_assignment9:add_item(PID, milk, 2.01).
%ok
%6> helminiak_assignment9:add_item(PID, lettuce, 0).
%ok
%7> helminiak_assignment9:show_shelves(PID).
%[{milk,2.01},{cupcake,0.75},{lettuce,0},{soup,2.35}]
%8> helminiak_assignment9:choose(PID, cupcake, 100).
%ok
%9> helminiak_assignment9:choose(PID, milk, 2).
%ok
%10> helminiak_assignment9:choose(PID, lettuce, 1).
%ok
%11> helminiak_assignment9:show_basket(PID).
%[{cupcake,100},{milk,2},{lettuce,1}]
%12> helminiak_assignment9:remove(PID, lettuce).
%ok
%13> helminiak_assignment9:show_basket(PID).
%[{cupcake,100},{milk,2}]
%14> helminiak_assignment9:checkout(PID).
%79.02
%15> 
%15> init:stop().
%ok

%===============================================================
%PROGRAM
%===============================================================

-module(helminiak_assignment9).
-export([start/0, send_wait/2, show_shelves/1, show_basket/1, choose/3, add_item/3, remove/2, checkout/1]).

%Spawn a PID for this server
start() -> spawn(fun init/0).

%Stock the initial shelf
init() ->
  put({item, lettuce}, 1.50),
  put({item, cupcake}, 0.75),
  put({item, soup}, 2.35),
  put(basket, []),
  loop().

%List all items in the shelf
shelf_list() ->
  K = get(),
  [{N,get(E)} || {{item, N}=E, _} <- K].

%Add items to the basket; update those that already exist
add_items(Name, Count, []) -> [{Name, Count}];
add_items(Name, Count, [{Name, C}|L]) -> [{Name, C+Count}|L];
add_items(Name, Count, [H|L]) -> [H|add_items(Name, Count, L)].

%Given the name and the list of the basket
remove_item(_, []) -> []; %If there is nothing in the basket return nothing
remove_item(Name, [{Name, _}]) -> []; %If the item is the only item in the basket, then return a null list
remove_item(Name, [{Name, _}|Remain]) -> [Remain]; %If the item is the first item in the basket, return the rest
remove_item(Name, [First|Remain]) -> [First | remove_item(Name, Remain)].  %Otherwise check the next item to see if its a match

%Find the price of an item
price_check(Name, []) -> 0; %If the shelves to check are empty, then the item doesn't cost anything
price_check(Name, [{Name, Price}|Remain]) -> Price; %If the item is the first item on the shelf, then return its price
price_check(Name, [First|Remain]) -> price_check(Name, Remain). %Otherwise check the next item on the shelf

%Calculate the price of the items within the basket
get_cost([]) -> 0; %An empty basket costs nothing
get_cost([{Name, Count}|Remain]) -> Count * price_check(Name, shelf_list()) + get_cost(Remain). %Add the price to the remainder

loop() ->
  receive
    {From, show_shelves} ->
          From ! {self(), shelf_list()}, loop();
    {From, show_basket} ->
          From ! {self(), get(basket)}, loop();
    {From, {choose, Name, Count}} -> 
          put(basket, add_items(Name, Count, get(basket))),
          From ! {self(), ok}, loop();
    {From, {add_item, Name, Price}} -> 
          put({item, Name}, Price),
          From ! {self(), ok}, loop();
    {From, {remove, Name}} -> 
          put(basket, remove_item(Name, get(basket))),
          From ! {self(), ok}, loop();
    {From, checkout} -> 
          From ! {self(), get_cost(get(basket))}, loop();
    {From, Other} ->
          From ! {self(), {error, Other}}, loop()
  end.

% Basic requests
show_shelves(PID) -> send_wait(PID, show_shelves).
show_basket(PID) -> send_wait(PID, show_basket). 
choose(PID, Name, Count) -> send_wait(PID, {choose, Name, Count}).
add_item(PID, Name, Price) -> send_wait(PID, {add_item, Name, Price}). 
remove(PID, Name) -> send_wait(PID, {remove, Name}).
checkout(PID) -> send_wait(PID, checkout). 

% core client function
send_wait(Pid, Request) ->
  Pid ! {self(), Request},
  receive
    {Pid, Response} -> Response
  end.