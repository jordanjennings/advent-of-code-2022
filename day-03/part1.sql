CREATE TABLE rucksacks(sack_content);
.separator ~
.import input.txt rucksacks

WITH
  items AS (
    WITH RECURSIVE split(sack_content, s, last, rest) AS (
      SELECT sack_content, '', '', sack_content FROM rucksacks
      UNION ALL
      SELECT sack_content,
             s || substr(rest, 1, 1),
             substr(rest, 1, 1),
             substr(rest, 2)
      FROM split
      WHERE rest <> ''
    )
    SELECT sack_content, last as item,
           iif(length(s) > length(sack_content)/2, 'right', 'left') AS compartment
    FROM split
    WHERE last != ''
  ),
  items_in_both_compartments AS (
    SELECT DISTINCT sack_content, i1.item AS repeat_item
    FROM items i1
           JOIN items i2 USING (item, sack_content)
    WHERE i1.compartment = 'left'
      AND i2.compartment = 'right'
  ),
  repeat_items AS (
    SELECT *,
           iif(repeat_item >= 'A' AND repeat_item <= 'Z',
               unicode(repeat_item) - unicode('A') + 27,
               unicode(repeat_item) - unicode('a') + 1) AS priority
    FROM items_in_both_compartments
  )
SELECT sum(priority) from repeat_items;

