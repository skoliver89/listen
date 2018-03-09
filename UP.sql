CREATE TABLE dbo.UserAccounts
(
   /* Populated with Google SID On first login
      Used as Primary Key for User Look-up */
  UserID  NVARCHAR(50)  NOT NULL UNIQUE,
  Username  NVARCHAR(20)  NOT NULL UNIQUE,
  Timezone  NVARCHAR(5) NULL,
  TimeFormat  NVARCHAR(5) NULL,
  Email NVARCHAR(50)  NOT NULL UNIQUE

  CONSTRAINT[PK_dbo.UserAccounts] PRIMARY KEY (UserID)
);

GO
