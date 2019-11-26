INSERT INTO workflows (name, version, country_code, workflow, user_id)
     VALUES ('Workflow 1', 1, 'CO', 'Workflow "Workflow 1" ruleset "test" test total >= 100 return allow default block end', '84b22591-7894-4063-943f-511a157409c3'),
            ('Workflow 1', 2, 'CO', 'Workflow "Workflow 1" ruleset "test" test total >= 200 return allow default block end', '40a91153-647b-4f67-9096-ed2d5c4cb186'),
            ('Workflow 1', 3, 'CO', 'Workflow "Workflow 1" ruleset "test" test total >= 300 return allow default block end', '7b120557-a437-4f55-b218-9da099c9a0b3')
         ON CONFLICT (country_code, name, version) DO UPDATE SET workflow = EXCLUDED.workflow;