Feature: DB operations
  DB operation steps

  @DbOps01 @DbOpsAll
  Scenario: update DB with CSV and validate - SUCCESS - with ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario1/input/"
    Then Expect 'testData/DbOps/scenario1/output/' with ignore columns
      | user_id |

  @DbOps02 @DbOpsAll
  Scenario: update DB with CSV and validate - ERROR - with ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario2/input/"
    Then Expect 'testData/DbOps/scenario2/output/' with ignore columns
      | user_id |

  @DbOps03 @DbOpsAll
  Scenario: update DB with CSV and validate - SUCCESS - without ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario3/input/"
    Then Expect 'testData/DbOps/scenario3/output/'

  @DbOps04 @DbOpsAll
  Scenario: update DB with CSV and validate - ERROR - without ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario4/input/"
    Then Expect 'testData/DbOps/scenario4/output/'