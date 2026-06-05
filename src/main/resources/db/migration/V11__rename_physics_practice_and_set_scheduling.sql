-- Rename the physics practice to a more appropriate name and ensure diary is required.

update practices
set name = 'Практикум по физике: эксперименты и наблюдения',
    description = 'Проведение серии лабораторных опытов по физике. Каждый день фиксируйте ход эксперимента, результаты измерений и выводы.',
    scheduling_required = true
where name = 'Лабораторный дневник по физике';

-- Also ensure the field practice has scheduling_required = true.
update practices
set scheduling_required = true
where name = 'Полевая практика: наблюдение физических явлений';
